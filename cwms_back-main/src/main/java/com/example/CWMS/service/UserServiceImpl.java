package com.example.CWMS.service;

import com.example.CWMS.dto.UserDTO;
import com.example.CWMS.model.*;
import com.example.CWMS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    @Autowired
    private SiteRepository siteRepository;
    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return mapToDTO(user);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();

        // 1. Génération d'un mot de passe temporaire de 8 caractères
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);

        // 2. Mapping des champs de base
        updateUserFields(user, userDTO);

        // 3. Hachage du mot de passe pour la sécurité
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true); // Activation par défaut

        User savedUser = userRepository.save(user);

        // 4. Envoi du mail avec les identifiants en clair (avant hachage)
        emailService.sendCredentials(savedUser.getEmail(), savedUser.getUsername(), rawPassword);

        return mapToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));


        updateUserFields(user, userDTO);
        user.setUpdatedAt(LocalDateTime.now());

        // Sauvegarde et retour
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    private void updateUserFields(User user, UserDTO dto) {
        if (dto.getUserName() != null) user.setUsername(dto.getUserName());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());

        // 1. GESTION DU RÔLE
        if (dto.getRoleName() != null && !dto.getRoleName().isEmpty()) {
            roleRepository.findAll().stream()
                    .filter(r -> r.getRoleName().equalsIgnoreCase(dto.getRoleName()))
                    .findFirst()
                    .ifPresent(user::setRole);
        }

        // 2. GESTION DU SITE (C'était la partie manquante !)
        if (dto.getSiteName() != null && !dto.getSiteName().isEmpty()) {
            siteRepository.findAll().stream()
                    .filter(s -> s.getSiteName().equalsIgnoreCase(dto.getSiteName()))
                    .findFirst()
                    .ifPresent(user::setSite);
        }
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getUserId());
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());

        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getRoleName());
        }
        if (user.getSite() != null) {
            dto.setSiteName(user.getSite().getSiteName());
        }
        return dto;
    }
}