package com.auth.Authorization.Service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.auth.Authorization.Components.UserInfoMapper;
import com.auth.Authorization.DTO.AuthResponseDto;
// import com.auth.Authorization.DTO.AuthResponseDto;
import com.auth.Authorization.DTO.TokenType;
import com.auth.Authorization.DTO.UserRegistrationDto;
import com.auth.Authorization.Model.RefreshTokenEntity;
import com.auth.Authorization.Model.UserInfo;
import com.auth.Authorization.Repository.RefreshTokenRepo;
import com.auth.Authorization.Repository.UserInfoRepo;

import jakarta.servlet.http.Cookie;
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
    private final UserInfoMapper userInfoMapper;
     public AuthResponseDto getJwtTokensAfterAuthentication(Authentication authentication, HttpServletResponse response) {
         try {
             var userInfoEntity = userInfoRepo.findByEmailId(authentication.getName())
                     .orElseThrow(() -> {
                         log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                         return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                     });

             String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
             String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);
             saveUserRefreshToken(userInfoEntity, refreshToken);
                
             creatRefreshTokenCookie(response,refreshToken);
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
     
     private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
         Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
         refreshTokenCookie.setHttpOnly(true);
         refreshTokenCookie.setSecure(true);
         refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // in seconds
         response.addCookie(refreshTokenCookie);
         return refreshTokenCookie;
     }

         public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {
 
         if(!authorizationHeader.startsWith(TokenType.Bearer.name())){
             return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Please verify your token type");
         }
 
         final String refreshToken = authorizationHeader.substring(7);
 
         //Find refreshToken from database and should not be revoked : Same thing can be done through filter.  
         var refreshTokenEntity = refreshTokenRepo.findByRefreshToken(refreshToken)
                 .filter(tokens-> !tokens.isRevoked())
                 .orElseThrow(()-> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Refresh token revoked"));
 
         UserInfo userInfoEntity = refreshTokenEntity.getUser();
         
         //Now create the Authentication object
         Authentication authentication =  createAuthenticationObject(userInfoEntity);
         String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
 
         return  AuthResponseDto.builder()
                 .accessToken(accessToken)
                 .accessTokenExpiry(5 * 60)
                 .userName(userInfoEntity.getUserName())
                 .tokenType(TokenType.Bearer)
                 .build();
     }
    
     private static Authentication createAuthenticationObject(UserInfo userInfoEntity) {
         // Extract user details from UserDetailsEntity
         String username = userInfoEntity.getEmailId();
         String password = userInfoEntity.getPassword();
         String roles = userInfoEntity.getRoles();

         // Extract authorities from roles (comma-separated)
         String[] roleArray = roles.split(",");
         GrantedAuthority[] authorities = Arrays.stream(roleArray)
                 .map(role -> (GrantedAuthority) role::trim)
                 .toArray(GrantedAuthority[]::new);

         return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
     }
     
     public AuthResponseDto registerUser(UserRegistrationDto userRegistrationDto,HttpServletResponse httpServletResponse){

        try{
            log.info("[AuthService:registerUser]User Registration Started with :::{}",userRegistrationDto);

            Optional<UserInfo> user = userInfoRepo.findByEmailId(userRegistrationDto.userEmail());
            if(user.isPresent()){
                throw new Exception("User Already Exist");
            }

            UserInfo userDetailsEntity = userInfoMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userDetailsEntity);


            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            UserInfo savedUserDetails = userInfoRepo.save(userDetailsEntity);
            saveUserRefreshToken(userDetailsEntity,refreshToken);
            
            creatRefreshTokenCookie(httpServletResponse,refreshToken);
            
            log.info("[AuthService:registerUser] User:{} Successfully registered",savedUserDetails.getUserName());
            return   AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .userName(savedUserDetails.getUserName())
                    .tokenType(TokenType.Bearer)
                    .build();


        }catch (Exception e){
            log.error("[AuthService:registerUser]Exception while registering the user due to :"+e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }

    }
     
 }
