package com.impact.lessons.controllers;/*package com.impact.lessons.controllers;

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
*/

import com.impact.lessons.dto.CreateUserRequest;
import com.impact.lessons.dto.UserResponse;
import com.impact.lessons.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request);
    }
    @GetMapping("/get_all")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

}
