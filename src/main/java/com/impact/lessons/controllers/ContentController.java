package com.impact.lessons.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content")
public class ContentController {

    @PostMapping("/create")
    public String create() {
        return "content created";
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('EDITOR')")
    public String update() {
        return "content updated";
    }

    @GetMapping("/get")
    public String get() {
        return "content get";
    }
}
