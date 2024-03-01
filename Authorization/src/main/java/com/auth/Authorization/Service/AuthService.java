package com.auth.Authorization.Service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.auth.Authorization.DTO.AuthResponseDto;
// import com.auth.Authorization.DTO.AuthResponseDto;
import com.auth.Authorization.DTO.TokenType;
import com.auth.Authorization.Model.RefreshTokenEntity;
import com.auth.Authorization.Model.UserInfo;
import com.auth.Authorization.Repository.RefreshTokenRepo;
import com.auth.Authorization.Repository.UserInfoRepo;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
 @RequiredArgsConstructor
 @Slf4j
 public class AuthService {
 
     private final UserInfoRepo userInfoRepo;
     private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepo refreshTokenRepo;
     public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
         try {
             var userInfoEntity = userInfoRepo.findByEmailId(authentication.getName())
                     .orElseThrow(() -> {
                         log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                         return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                     });

             String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
             String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
                saveUserRefreshToken(userInfoEntity,refreshToken);
             log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",
                     userInfoEntity.getUserName());
             return AuthResponseDto.builder()
                     .accessToken(accessToken)
                     .accessTokenExpiry(15 * 60)
                     .userName(userInfoEntity.getUserName())
                     .tokenType(TokenType.Bearer)
                     .build();

         } catch (Exception e) {
             log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :" + e.getMessage());
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");
         }

     }
     
       private void saveUserRefreshToken(UserInfo userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshTokenEntity.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepo.save(refreshTokenEntity);
    }
     
     
 }
