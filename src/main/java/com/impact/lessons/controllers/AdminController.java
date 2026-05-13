package com.impact.lessons.controllers;

import com.impact.lessons.dto.UserListResponse;
import com.impact.lessons.dto.UserResponse;
import com.impact.lessons.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/get_all")
    public UserListResponse getAllUsers() {
        return userService.getAllUsers();
    }
}