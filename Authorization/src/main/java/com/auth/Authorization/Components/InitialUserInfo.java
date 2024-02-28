package com.auth.Authorization.Components;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.auth.Authorization.Model.UserInfo;
import com.auth.Authorization.Repository.UserInfoRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitialUserInfo implements CommandLineRunner{
    private final UserInfoRepo userInfoRepo ;
    private final PasswordEncoder passwordEncoder ;
    @Override
    public void run(String... args) throws Exception {
        UserInfo manager = new UserInfo();
        manager.setUserName("Manager");
        manager.setPassword(passwordEncoder.encode("password"));
        manager.setRoles("ROLE_MANAGER");
        manager.setEmailId("manager@manager.com");
        
        UserInfo admin = new UserInfo();
        admin.setUserName("Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles("ROLE_ADMIN");
        admin.setEmailId("admin@admin.com");

        UserInfo user = new UserInfo();
        user.setUserName("User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles("ROLE_USER");
        user.setEmailId("user@user.com");

        userInfoRepo.saveAll(List.of(manager,admin,user));
    }
}
