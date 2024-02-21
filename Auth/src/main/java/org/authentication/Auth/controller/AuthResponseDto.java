package org.authentication.Auth.controller;
// import org.authentication.Auth.controller.AuthStatus;


public record AuthResponseDto(Object token, AuthStatus authStatus) {
} 
