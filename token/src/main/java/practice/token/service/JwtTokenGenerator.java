package practice.token.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import lombok.RequiredArgsConstructor;
import practice.token.model.UserInfo;

@Service
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private final JwtEncoder jwtEncoder;

    public String generateAccessToken(UserInfo userInfo) {
        String authorities = userInfo.getAuthority();
        System.out.println("USERINFOCLAIMS    " + userInfo);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("atquil")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
                .subject(userInfo.getEmail())
                .claim("scope", authorities)
                .build();
        System.out.println("CLAIMS    " + claims);
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
