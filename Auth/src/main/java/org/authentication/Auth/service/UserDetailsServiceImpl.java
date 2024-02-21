package org.authentication.Auth.service;
import org.authentication.Auth.repo.AppUserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepo appUserRepo;
    @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        var appUser = appUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
      
        return new User(appUser.getUsername(), appUser.getPassword(),appUser.getAuthorities());
    }
    
}
