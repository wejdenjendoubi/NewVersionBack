package com.example.CWMS.service;

import com.example.CWMS.model.Role;
import com.example.CWMS.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Récupérer tous les rôles (utile pour l'interface d'assignation)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Trouver un rôle par ID
    public Optional<Role> getRoleById(int id) {
        return roleRepository.findById(id);
    }

    // Créer ou mettre à jour un rôle
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}