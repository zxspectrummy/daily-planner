package com.example.dailyplanner.controller;

import com.example.dailyplanner.model.User;
import com.example.dailyplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) throws ResourceNotFoundException {
        return userService.getById(id);
    }

    @GetMapping("/users/me")
    public User getCurrentUser() throws ResourceNotFoundException {
        return userService.getCurrentUser();
    }
}
