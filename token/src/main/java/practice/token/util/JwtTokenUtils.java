package practice.token.util;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Objects;
import practice.token.configuration.UserInfoConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import lombok.RequiredArgsConstructor;
import practice.token.repo.UserInfoRepo;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    public String getUserName(Jwt jwtToken) {
        System.out.println("TOKEEENNNNNEEEEJ" + jwtToken);

        return jwtToken.getSubject();
    }

    public boolean tokenValid(Jwt jwtToken, UserDetails userDetails) {
        final String userName = getUserName(jwtToken);
        boolean tokenExpired = getIfTokenIsExpired(jwtToken);
        boolean tokenUserSame = userName.equals(userDetails.getUsername());
        return !tokenExpired && tokenUserSame;
    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    private final UserInfoRepo useruserInfoRepo;

    public UserDetails userDetails(String email) {
        System.out.println("USERINFO EMAI:    " + email);
        return useruserInfoRepo
                .findByEmail(email)
                .map(UserInfoConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("UserEmail: " + email + " does not exist"));
    }
}
