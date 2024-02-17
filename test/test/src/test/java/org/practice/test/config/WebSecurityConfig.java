package org.practice.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
    private static final String[] WHITELIST = {
            "/",
            "/userInfo",
        "/allUser"
    };

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
       http.csrf(AbstractHttpConfigurer::disable)
		.authorizeHttpRequests((authorize) -> authorize
			.requestMatchers(WHITELIST).permitAll()
			
            );
        return http.build();
    }
    
}
