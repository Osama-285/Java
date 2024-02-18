package org.practice.test.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var jwtToken = getTokenFromRequest(request);
    }

        private String getTokenFromRequest(HttpServletRequest request){
            var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
          if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }

        throw new UnsupportedOperationException("Unimplemented method 'doFilterInternal'");
    }
 
}
