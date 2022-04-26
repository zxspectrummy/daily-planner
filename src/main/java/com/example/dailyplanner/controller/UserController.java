package com.example.dailyplanner.controller;

import com.example.dailyplanner.model.User;
import com.example.dailyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public User create(@RequestBody User user) throws Exception {
        return userService.saveOrUpdate(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) throws ResourceNotFoundException {
        return userService.getById(id);
    }

    @GetMapping("/users/me")
    public User getCurrentUser() throws ResourceNotFoundException {
        return userService.getCurrentUser();
    }
}
