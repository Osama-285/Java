package org.practice.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.model.Model;

@RestController
public class RouteController {
    @GetMapping("/")
    public String responseData() {
        return "Success";
    }

    @PostMapping("/addInfo")
    public ResponseEntity<Void> submitData(@RequestBody Model data) {
        
             return ResponseEntity.ok().build();
        }
    
}
