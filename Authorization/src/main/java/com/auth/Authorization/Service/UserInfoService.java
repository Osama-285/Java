package com.auth.Authorization.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.Authorization.Config.UserInfoConfig;
import com.auth.Authorization.Repository.UserInfoRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepo userInfoRepo;

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        System.out.println("USERR"+emailId);
        return userInfoRepo.findByEmailId(emailId).map(UserInfoConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("UserEmail: "+emailId+" does not exist"));
        
    }
    
}
