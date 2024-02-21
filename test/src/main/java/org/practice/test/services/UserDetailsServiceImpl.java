package org.practice.test.services;

import org.practice.test.repository.AuthUserRepo;
import org.practice.test.model.AppUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AuthUserRepo authUserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        var appUser = authUserRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
      
        return new User(appUser.getUsername(), appUser.getPassword(),appUser.getAuthorities());
    }
    
}
