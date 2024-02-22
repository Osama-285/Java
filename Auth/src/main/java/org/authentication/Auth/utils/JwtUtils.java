package org.authentication.Auth.utils;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.time.DateUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

private JwtUtils() {
        
    }
    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final String ISSUER = "Testing_ABC_ASDEADa";
    public static boolean validateToken(String jwtToken) {
        // System.out.println("VALIDATETOKEN"+jwtToken);
        return parseToken(jwtToken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {

        var jwtParser = Jwts.parser().verifyWith(secretKey).build();
        System.out.println("SECRETTTETETET" + jwtParser);
        // System.out.println("TOKENNNNNNN"+jwtToken);
        // System.out.println("PARSERRRRRRRRR"+jwtParser.parseSignedClaims(jwtToken));

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException e) {
            log.error("JWT Exception Occured"+ e);

        }
        return Optional.empty();

    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {
System.out.println("getUsernameFromToken"+jwtToken);
        var claimsOptional = parseToken(jwtToken);

        return claimsOptional.map(Claims::getSubject);

    }

    

    public static String generateToken(String username) {

        var currentDate = new Date();
        var jwtExpirationMinutes = 10;
      var expiration = DateUtils.addMinutes(currentDate, jwtExpirationMinutes);
        
       return Jwts.builder().id(UUID.randomUUID().toString()).issuer(ISSUER).subject(username).signWith(secretKey).issuedAt(currentDate).expiration(expiration).compact();
    }
    
}
