package com.example.CWMS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/users") // Préfixe pour les utilisateurs normaux
public class UserController {

    @GetMapping("/home") // On change "dashboard" par "home" ou "profile"
    public ResponseEntity<?> getUserHome() {
        return ResponseEntity.ok("Bienvenue sur votre espace utilisateur !");
    }
}