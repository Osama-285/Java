package org.practice.test.controller;
import java.util.List;
import java.util.Optional;
import org.practice.test.model.User;
import org.practice.test.repository.AddUserRepo;
import org.practice.test.services.AddUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class RouteController {
    private String userEmail;
    private String delUser;
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
        System.out.println(user.getEmail() + "USER POST" + user);
        userService.addUser(user);

        return ResponseEntity.ok().build();
    }
       
    @PutMapping("/addInfo")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        userEmail = updatedUser.getEmail();
        Optional<User> optionalUser = userRepo.findByEmail(userEmail);
        System.out.println("USERINfo " + optionalUser);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println(user);
            user.setUsername(updatedUser.getUsername());
            user = userRepo.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/addInfo")
    public ResponseEntity<User> deleteUser(@RequestBody User userDel) {
     delUser = userDel.getEmail();
     Optional<User> optionalUser = userRepo.findByEmail(delUser);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
          userRepo.delete(user);
        return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userRepo.findAll();
        return ResponseEntity.ok(users);
    }

    
}
