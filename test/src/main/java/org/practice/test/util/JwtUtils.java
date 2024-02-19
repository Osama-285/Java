package org.practice.test.util;

import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.security.oauth2.jwt.JwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {
    private JwtUtils() {
        
    }
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public static boolean validateToken(String jwtToken) {

        return parseToken(jwtToken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {

        var jwtParser = Jwts.parser().verifyWith(secretKey).build();

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException e) {
            log.error("JWT Exception Occured");

        }
        return Optional.empty();

    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {

        var claimsOptional = parseToken(jwtToken);

        return claimsOptional.map(Claims::getSubject);

    }
}