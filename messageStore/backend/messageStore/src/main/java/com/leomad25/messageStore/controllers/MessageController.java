package com.leomad25.messageStore.controllers;

import com.leomad25.messageStore.models.MessageModel;
import com.leomad25.messageStore.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService service;

    @GetMapping
    public MessageModel getMessage(@RequestParam long cedula) {
        return service.get(cedula);
    }

    @PostMapping
    public String addMessage(@RequestParam long cedula, @RequestBody String message) {
        return service.add(cedula, message);
    }

    @DeleteMapping
    public String deleteMessage(@RequestParam long cedula) {
        return service.delete(cedula);
    }
}
