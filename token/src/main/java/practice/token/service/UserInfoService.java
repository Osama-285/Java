package practice.token.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import practice.token.util.TokenType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import practice.token.model.UserInfo;
import practice.token.repo.UserInfoRepo;
import practice.token.util.AuthResponseDTO;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {

    @Autowired
    private UserInfoRepo userInfoRepo;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    public AuthResponseDTO save(UserInfo userInfo) {
        System.out.println("PASSWORDDDDDD" + userInfo.getPassword());
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        UserInfo savedUser = userInfoRepo.save(userInfo);
        AuthResponseDTO userToken = authenticateUser(savedUser);
        System.out.println("USER AUTHTOKEN" + userToken);
        return userToken;
    }

    public AuthResponseDTO authenticateUser(UserInfo userInfo) {
        String username = userInfo.getUsername();
        System.out.println("AUTHETICATE USER  " + userInfo);
        String accessToken = jwtTokenGenerator
                .generateAccessToken(userInfo);

        System.out.println("ACCESSTOKEN  " + accessToken);
        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(15 * 60)
                .userName(username) // Consider consistency with obtained username
                .tokenType(TokenType.Bearer)
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

}
