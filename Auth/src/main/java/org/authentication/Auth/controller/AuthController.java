package org.authentication.Auth.controller;

import org.authentication.Auth.service.AuthService;
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
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDto> signup(@RequestBody AuthRequestDto authRequestDto) {
        try {
                System.out.println("TESSSSSSTTTTSAFSDASDASDADASDas"+authRequestDto);
                var jwtToken = authService.signup(authRequestDto.name(), authRequestDto.username(),
                        authRequestDto.password());
                System.out.println("TOKENN"+jwtToken);
                var authResponseDto = new AuthResponseDto(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY);
    return ResponseEntity.status(HttpStatus.OK).body(authResponseDto);
            } catch (Exception e) {
              System.out.println("ERROR"+e);
                var authResponseDto = new AuthResponseDto(null, AuthStatus.USER_NOT_CREATED);
               return ResponseEntity.status(HttpStatus.CONFLICT).body(authResponseDto);
            }
    }
}
