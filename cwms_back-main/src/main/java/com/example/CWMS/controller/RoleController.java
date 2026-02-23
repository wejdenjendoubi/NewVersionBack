package com.example.CWMS.controller;

import com.example.CWMS.model.Role;
import com.example.CWMS.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*") // Permet la connexion avec Angular
public class RoleController {

    @Autowired
    private RoleService roleService;

    // GET /api/roles : Pour remplir la liste des rôles dans l'interface Admin
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    // POST /api/roles : Pour créer un nouveau rôle
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }
}