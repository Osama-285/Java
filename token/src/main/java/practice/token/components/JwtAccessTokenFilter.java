package practice.token.components;

import java.io.IOException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import practice.token.configuration.RSAKeyRecord;
import practice.token.model.UserInfo;
import practice.token.util.JwtTokenUtils;
import practice.token.util.TokenType;

@RequiredArgsConstructor
public class JwtAccessTokenFilter extends OncePerRequestFilter {
    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            System.out.println("Authorization:  " + authHeader);

            JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

            System.out.println("JWTDECODER:  " + jwtDecoder);

            if (!authHeader.startsWith(TokenType.Bearer.name())) {
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("BEARER TOKEN " + TokenType.Bearer.name());
            final String token = authHeader.substring(7);
            final Jwt jwtToken = jwtDecoder.decode(token);

            final String userName = jwtTokenUtils.getUserName(jwtToken);
            System.out
                    .println("USERNAME    :   " + userName);

            UserDetails userDetails = jwtTokenUtils.userDetails(userName);
            System.out.println("USERTOKENNNNNNNN" + userDetails);

            if (!userName.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null);
                createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(createdToken);
                SecurityContextHolder.setContext(securityContext);
            }

            filterChain.doFilter(request, response);
        } catch (JwtValidationException jwtValidationException) {
            // log.error("[JwtAccessTokenFilter:doFilterInternal] Exception due to
            // :{}",jwtValidationException.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, jwtValidationException.getMessage());
        }
    }

}
