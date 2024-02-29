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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth.Authorization.Service.UserInfoService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    UserInfoService userInfoService;

   
    
    @Bean
    public SecurityFilterChain webSecurity(HttpSecurity httpSecurity) throws Exception {
        System.out.println("userINFO"+httpSecurity);
        return httpSecurity.securityMatcher(new AntPathRequestMatcher("/api/**")).csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest()
                        .authenticated()).userDetailsService(userInfoService)

                .build();
    }

@Bean
public SecurityFilterChain web(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(
                sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
           return httpSecurity.build();

    }


@Bean
PasswordEncoder passwordEncoder() {
return new BCryptPasswordEncoder();
}
}
