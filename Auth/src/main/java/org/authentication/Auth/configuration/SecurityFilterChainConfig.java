package org.authentication.Auth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityFilterChainConfig {
    private final AuthenticationEntryPoint authenticationEntryPoint;
     private final JwtAuthenticationFilter JwtAuthenticationFilter; 
    

    public SecurityFilterChainConfig(AuthenticationEntryPoint authenticationEntryPoint,
            org.authentication.Auth.configuration.JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        JwtAuthenticationFilter = jwtAuthenticationFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
           httpSecurity.cors(AbstractHttpConfigurer::disable); 

       httpSecurity.csrf(AbstractHttpConfigurer::disable);
		httpSecurity.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/auth/login/**").permitAll()
			.requestMatchers("/api/auth/sign-up/**").permitAll()
			
        );
            
        httpSecurity.exceptionHandling(
                exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint));

        httpSecurity.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        
        httpSecurity.addFilterBefore(JwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
         return httpSecurity.build(); 
    }
    }

