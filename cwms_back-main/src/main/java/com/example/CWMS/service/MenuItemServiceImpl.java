package com.example.CWMS.service;

import com.example.CWMS.audit.Auditable;
import com.example.CWMS.dto.MenuItemDTO;
import com.example.CWMS.iservice.MenuItemService;
import com.example.CWMS.model.MenuItem;
import com.example.CWMS.model.User;
import com.example.CWMS.repository.MenuItemRepository;
import com.example.CWMS.repository.RoleMenuMappingRepository;
import com.example.CWMS.repository.RoleRepository;
import com.example.CWMS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    private final RoleMenuMappingRepository roleMenuMappingRepository;
    private final RoleRepository roleRepository;

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
    @Override
    @Transactional
    @Auditable(action = "MENU_CREATED", entityType = "MenuItem")
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
    @Override
    @Transactional
    @Auditable(action = "MENU_UPDATED", entityType = "MenuItem")
    public MenuItemDTO updateMenuItem(Integer id, MenuItemDTO request) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuItem not found: " + id));
        if (request.getLabel() != null) item.setLabel(request.getLabel());
        if (request.getIcon() != null) item.setIcon(request.getIcon());
        if (request.getLink() != null) item.setLink(request.getLink());
        if (request.getParentId() != null) item.setParent(request.getParentId());
        return toDTO(menuItemRepository.save(item));
    }

    @Override
    @Transactional
    @Auditable(action = "MENU_DELETED", entityType = "MenuItem")
    public void deleteMenuItem(Integer id) {
        menuItemRepository.deleteById(id);
    }

    public MenuItemDTO toDTO(MenuItem item) {
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
    @Override
    @Transactional
    @Auditable(action = "MENU_SAVED",entityType = "MenuItem")
    public void saveRoleMenuMappings(Integer roleId, List<Integer> menuItemIds) {
        // 1. Supprimer les anciens accès pour ce rôle
        roleMenuMappingRepository.deleteByRoleId(roleId);

        // 2. Récupérer l'entité Role
        com.example.CWMS.model.Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));

        // 3. Créer les nouveaux mappings
        List<com.example.CWMS.model.RoleMenuMapping> mappings = menuItemIds.stream()
                .map(menuId -> {
                    com.example.CWMS.model.MenuItem item = menuItemRepository.findById(menuId)
                            .orElseThrow(() -> new RuntimeException("Menu " + menuId + " non trouvé"));

                    return com.example.CWMS.model.RoleMenuMapping.builder()
                            .role(role)
                            .menuItem(item)
                            .build();
                })
                .collect(Collectors.toList());

        roleMenuMappingRepository.saveAll(mappings);
    }

    // Ajoutez également cette méthode pour récupérer les IDs déjà cochés dans l'interface
    public List<Integer> getMenuItemIdsForRole(Integer roleId) {
        return roleMenuMappingRepository.findMenuItemIdsByRoleId(roleId);
    }

}