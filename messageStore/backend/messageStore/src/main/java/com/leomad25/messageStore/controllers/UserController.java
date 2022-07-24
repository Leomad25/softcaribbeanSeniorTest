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
    public UserModel getUser(@RequestParam String cedula) {
        long cedulaLong = -1;
        try { cedulaLong = Long.valueOf(cedula); } catch (Exception ex) { return null; }
        if (cedulaLong > 0) return service.get(cedulaLong);
        return null;
    }

    @PostMapping
    public UserModel addUser(@RequestParam String cedula, @RequestParam String nombre, @RequestParam String apellido) {
        long cedulaLong = -1;
        try { cedulaLong = Long.valueOf(cedula); } catch (Exception ex) { return null; }
        if (nombre == "") nombre = "Sin Nombre";
        if (apellido == "") apellido = "Sin Apellido";
        UserModel user = new UserModel(cedulaLong, nombre, apellido);
        String result = service.add(user);
        if (result.equals("User Inserted") || result.equals("User updated")) return service.get(cedulaLong);
        return null;
    }

    @DeleteMapping
    public void deleteUser(@RequestParam String cedula) {
        long cedulaLong = -1;
        try { cedulaLong = Long.valueOf(cedula); } catch (Exception ex) { return; }
        if (cedulaLong > 0) service.delete(cedulaLong);
        return;
    }
}