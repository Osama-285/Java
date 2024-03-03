package practice.token.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import practice.token.model.UserInfo;
import practice.token.service.UserInfoService;
import practice.token.util.UserInfoDTO;

@RestController
public class AuthController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping(value = "/signup", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserInfoDTO user) {

        UserInfo userInfo = new UserInfo();
        userInfo.setName(user.getName());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());
        userInfo.setPassword(user.getPassword());
        userInfo.setAuthority(user.getAuthorities());
        Object userData = userInfoService.save(userInfo);
        return ResponseEntity.ok(userData); // Return the created user object
    }
}
