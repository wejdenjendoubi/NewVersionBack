package com.example.CWMS.dto;

import java.util.Date;

public class RoleDTO {
    private int roleId;
    private String roleName;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    // Constructeurs
    public RoleDTO() {}

    // Getters et Setters
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}