package com.auth.Authorization.Service;

import javax.sound.midi.Soundbank;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.Authorization.Config.UserInfoConfig;
import com.auth.Authorization.Repository.UserInfoRepo;

@Service
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepo userInfoRepo = null;

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        System.out.println("USERR"+emailId);
        return userInfoRepo.findByEmailId(emailId).map(UserInfoConfig::new)
                .orElseThrow(() -> new UsernameNotFoundException("UserEmail: "+emailId+" does not exist"));
        
    }
    
}
