package com.example.CWMS.controller;

import com.example.CWMS.dto.UserDTO;
import com.example.CWMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin") // Protection globale par ROLE_ADMIN dans SecurityConfig
public class AdminController {

    @Autowired
    private UserService userService;

    // 1. Dashboard : Simple message de bienvenue ou stats
    @GetMapping("/dashboard")
    public ResponseEntity<String> getAdminStats() {
        return ResponseEntity.ok("Bienvenue sur le panneau d'administration. Accès réservé aux administrateurs.");
    }

    // 2. Liste de tous les utilisateurs
    // URL: GET /api/admin/users
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 3. Récupérer un utilisateur spécifique par son ID
    // URL: GET /api/admin/users/1
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // 4. Créer un nouvel utilisateur (ex: Admin qui crée un employé)
    // URL: POST /api/admin/users
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 5. Modifier un utilisateur
    // URL: PUT /api/admin/users/1
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 6. Supprimer un utilisateur
    // URL: DELETE /api/admin/users/1
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}