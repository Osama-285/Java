package com.auth.Authorization.Components;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.auth.Authorization.DTO.UserRegistrationDto;
import com.auth.Authorization.Model.UserInfo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserInfoMapper {
        private final PasswordEncoder passwordEncoder;
    public UserInfo convertToEntity(UserRegistrationDto userRegistrationDto) {
        UserInfo userInfoEntity = new UserInfo();
        userInfoEntity.setUserName(userRegistrationDto.userName());
        userInfoEntity.setEmailId(userRegistrationDto.userEmail());
        userInfoEntity.setMobileNumber(userRegistrationDto.userMobileNo());
        userInfoEntity.setRoles(userRegistrationDto.userRole());
        userInfoEntity.setPassword(passwordEncoder.encode(userRegistrationDto.userPassword()));
        return userInfoEntity;
    }
}
