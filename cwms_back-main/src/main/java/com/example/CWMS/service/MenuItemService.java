package com.example.CWMS.service;

import com.example.CWMS.dto.MenuItemDTO;
import com.example.CWMS.model.MenuItem;
import com.example.CWMS.model.User;
import com.example.CWMS.repository.MenuItemRepository;
import com.example.CWMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public List<MenuItemDTO> getMenuItemsForCurrentUser() {
        // 1. Récupérer l'email de l'utilisateur connecté via le SecurityContext
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Récupérer les menus associés à son RoleId via la table de mapping
        return menuItemRepository.findMenuItemsByRoleId(user.getRole().getRoleId())
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemDTO createMenuItem(MenuItemDTO request) {
        MenuItem item = MenuItem.builder()
                .label(request.getLabel())
                .icon(request.getIcon())
                .link(request.getLink())
                .parent(request.getParentId())
                .isTitle(request.getIsTitle() != null ? request.getIsTitle() : false)
                .isLayout(request.getIsLayout() != null ? request.getIsLayout() : false)
                .build();
        return toDTO(menuItemRepository.save(item));
    }

    @Transactional
    public MenuItemDTO updateMenuItem(Integer id, MenuItemDTO request) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + id));
        if (request.getLabel() != null) item.setLabel(request.getLabel());
        if (request.getIcon() != null) item.setIcon(request.getIcon());
        if (request.getLink() != null) item.setLink(request.getLink());
        if (request.getParentId() != null) item.setParent(request.getParentId());
        return toDTO(menuItemRepository.save(item));
    }

    @Transactional
    public void deleteMenuItem(Integer id) {
        menuItemRepository.deleteById(id);
    }

    private MenuItemDTO toDTO(MenuItem item) {
        return MenuItemDTO.builder()
                .menuItemId(item.getMenuItemId())
                .label(item.getLabel())
                .icon(item.getIcon())
                .link(item.getLink())
                .parentId(item.getParent())
                .isTitle(item.getIsTitle())
                .isLayout(item.getIsLayout())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}