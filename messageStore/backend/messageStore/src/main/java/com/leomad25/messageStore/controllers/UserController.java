package com.leomad25.messageStore.controllers;

import com.leomad25.messageStore.models.UserModel;
import com.leomad25.messageStore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public UserModel getUser(@RequestBody long cedula) {
        return service.get(cedula);
    }

    @PostMapping
    public String addUser(@RequestBody UserModel user) {
        return service.add(user);
    }

    @DeleteMapping
    public String deleteUser(@RequestBody long cedula) {
        return service.delete(cedula);
    }
}
