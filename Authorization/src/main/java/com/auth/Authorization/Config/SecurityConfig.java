package com.auth.Authorization.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import static org.springframework.security.config.Customizer.withDefaults;
import com.auth.Authorization.Service.UserInfoService;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    
    private final UserInfoService userInfoService;
    private final RSAKeyRecord rsaKeyRecord;

     @Order(1)
     @Bean
     public SecurityFilterChain signInSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
         return httpSecurity
                 .securityMatcher(new AntPathRequestMatcher("/sign-in/**"))
                 .csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                 .userDetailsService(userInfoService)
                 .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .exceptionHandling(ex -> {
                     ex.authenticationEntryPoint((request, response, authException) ->
                             response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage()));
                 }).httpBasic(withDefaults())
                 
                 .build();
     }
     @Order(2)
    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity httpSecurity) throws Exception {
        System.out.println("userINFO "+httpSecurity);
        return httpSecurity
             .securityMatcher(new AntPathRequestMatcher("/api/**"))
             .csrf(AbstractHttpConfigurer::disable)
             .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
             .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
             .exceptionHandling(ex -> {
                 log.error("[SecurityConfig:apiSecurityFilterChain] Exception due to :{}",ex);
                 ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint());
                 ex.accessDeniedHandler(new BearerTokenAccessDeniedHandler());
             })
             .httpBasic(withDefaults())
             .build();
    }
//  @Order(3)
// @Bean
// public SecurityFilterChain web(HttpSecurity httpSecurity) throws Exception {
//         httpSecurity.sessionManagement(
//                 sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//            return httpSecurity.build();

//     }


@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
   @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(rsaKeyRecord.rsaPublicKey()).privateKey(rsaKeyRecord.rsaPrivateKey()).build();
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }
}
