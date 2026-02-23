package com.example.CWMS.repository;

import com.example.CWMS.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // AJOUTE CETTE LIGNE : Indispensable pour trouver l'utilisateur au moment du Sign In
    Optional<User> findByUserName(String username);
}