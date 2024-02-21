package org.practice.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter JwtAuthenticationFilter;

    public WebSecurityConfig(AuthenticationEntryPoint authenticationEntryPoint,
            org.practice.test.config.JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        JwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    private static final String[] WHITELIST = {
        //     "/",
        //     "/addInfo",
        // "/allUser"
        // "/api/auth/login/**","/api/auth/sign-up/**"
    };

    @Bean
   public SecurityFilterChain web(HttpSecurity http) throws Exception {

        http.cors(AbstractHttpConfigurer::disable); 

       http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/api/auth/login/**").permitAll()
			.requestMatchers("/api/auth/sign-up/**").permitAll()
			
        );
            
        http.exceptionHandling(
                exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint));
        
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(JwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build(); 
    }
    
}
