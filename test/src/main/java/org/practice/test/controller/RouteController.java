package org.practice.test.controller;

import java.util.List;

import org.practice.test.model.User;
import org.practice.test.repository.AddUserRepo;
import org.practice.test.services.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.model.Model;

@RestController
public class RouteController {

     @Autowired
     private AddUserRepo userRepo;

     @Autowired
     private AddUserService userService;


    @GetMapping("/")
    public String responseData() {
        return "Success";
    }

    @PostMapping("/addInfo")
    public ResponseEntity<Void> submitData(@RequestBody User user) {
        System.out.println(user.getEmail());
        userService.addUser(user);

        return ResponseEntity.ok().build();
    }
        
    @GetMapping("/allUser")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
    }

    
}
