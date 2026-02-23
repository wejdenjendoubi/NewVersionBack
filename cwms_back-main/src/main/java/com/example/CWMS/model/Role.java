package com.example.CWMS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Roles")
public class Role {
    @Id
    private int RoleId;

    @Column(name = "RoleName")
    private String RoleName;

    @Column(name = "Description")
    private String Description;

    @Column(name="CreatedAt")
    private Date CreatedAt;

    @Column(name="UpdatedAt")
    private Date UpdatedAt;

    // Constructeurs
    public Role() {}

    // Getters et Setters (Indispensables pour Spring Boot)
    public int getRoleId() { return RoleId; }
    public void setRoleId(int roleId) { this.RoleId = roleId; }

    public String getRoleName() { return RoleName; }
    public void setRoleName(String roleName) { this.RoleName = roleName; }

    public String getDescription() { return Description; }
    public void setDescription(String description) { this.Description = description; }

    public Date getCreatedAt() { return CreatedAt; }
    public void setCreatedAt(Date createdAt) { this.CreatedAt = createdAt; }

    public Date getUpdatedAt() { return UpdatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.UpdatedAt = updatedAt; }
}