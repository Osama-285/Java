package org.practice.test.controller;

import org.practice.test.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());
     var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.LOGIN_SUCCESS);
     return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto authRequestDto) {
            try {
                var jwtToken = authService.signup(authRequestDto.name(),authRequestDto.username(), authRequestDto.password());
    var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY);
    return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
            } catch (Exception e) {
              
                var authResponseDto = new AuthResponseDto(null, AuthStatus.USER_NOT_CREATED);
               return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponseDto);
            }
    }
}
