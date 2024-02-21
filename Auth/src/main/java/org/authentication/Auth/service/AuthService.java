package org.authentication.Auth.service;

public interface AuthService {

    String login(String username, String password);

    String signup(String name, String username, String password);
    
}
