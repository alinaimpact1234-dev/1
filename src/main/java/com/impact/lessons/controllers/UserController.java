package com.impact.lessons.controllers;

import com.impact.lessons.dto.UpdatePasswordRequest;
import com.impact.lessons.models.User;
import com.impact.lessons.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService = new UserService();

    @PostMapping("/users/create")
    public User createUser(@RequestBody User user){
        return userService.CreateUser(user);
    }
    @GetMapping("/users/get_all")
    public List<User> GetAllUsers(){
        return userService.GetAllUsers();
    }
    @GetMapping("/users/get_by_id")    
    public Optional<User> GetUserByID(@RequestParam Long id){  
        return userService.GetUserById(id); 
    }
    @PutMapping("/user/update/{id}")
    public ResponseEntity<User> UpdateUser(@PathVariable Long id, @RequestBody User user){
        return userService.UpdateUser(id, user).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PatchMapping("/users/password/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable Long id, @RequestBody UpdatePasswordRequest request){
        return userService.UpdatePassword(id, request.getPassword()).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
}
    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Boolean> updatePassword(@PathVariable Long id){
        return userService.disableUser(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }
}
