package com.leomad25.messageStore.controllers;

import com.leomad25.messageStore.MessageStoreApplication;
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
    public MessageModel getMessage(@RequestParam String cedula) {
        long cedulaLong = -1;
        try { cedulaLong = Long.valueOf(cedula); } catch (Exception ex) { return null; }
        if (cedulaLong > 0) return service.get(cedulaLong);
        return null;
    }

    @PostMapping
    public MessageModel addMessage(@RequestParam String cedula, @RequestParam String message) {
        long cedulaLong = -1;
        try { cedulaLong = Long.valueOf(cedula); } catch (Exception ex) { return null; }
        if (message == "") return null;
        String result = service.add(cedulaLong, message);
        if (result.equals("Message inserted") || result.equals("Message updated")) return MessageStoreApplication.bPlusTreeMessage.search(cedulaLong);
        return null;
    }

    @DeleteMapping
    public void deleteMessage(@RequestParam String cedula, @RequestParam String pos) {
        long cedulaLong = -1;
        int posInt = -1;
        try { cedulaLong = Long.valueOf(cedula); } catch (Exception ex) { return; }
        if (pos.equals("-a")) {
            service.delete(cedulaLong);
        } else {
            try { posInt = Integer.valueOf(pos); } catch (Exception ex) { return; }
            if (cedulaLong > 0) service.delete(cedulaLong, posInt);
        }
    }
}
