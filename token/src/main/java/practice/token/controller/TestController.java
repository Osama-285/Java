package practice.token.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/welcome")
    public ResponseEntity<String> getFirstWelcomeMessage(Authentication authentication) {
        return ResponseEntity.ok("Success");
    }

}
