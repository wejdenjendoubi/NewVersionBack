package com.example.CWMS.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer UserId;
    @Column (name="Username")
    private String userName;
    @Column(name="Email")
    private String Email;
    @Column(name="PasswordHash")
    private String PasswordHash;
    @Column(name= "FirstName")
    private String FirstName;
    @Column(name= "LastName")
    private String LastName;
    @Column(name="IsActive")
    private  int IsActive;
    @Column(name="CreatedAt")
    private Date CreatedAt;
    @Column(name="UpdatedAt")
    private Date UpdatedAt;
    // Un user a UN SEUL rôle
    @ManyToOne
    @JoinColumn(name = "RoleId", nullable = false)
    private Role role;

    // Un user appartient à UN SEUL site
    @ManyToOne
    @JoinColumn(name = "IdSite") //
    private Site site;

    public User() {
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "User{" +
                "UserId=" + UserId +
                ", UserName='" + userName + '\'' +
                ", Email='" + Email + '\'' +
                ", PasswordHash='" + PasswordHash + '\'' +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", IsActive=" + IsActive +
                ", CreatedAt=" + CreatedAt +
                ", UpdatedAt=" + UpdatedAt +
                ", role=" + role +
                ", site=" + site +
                '}';
    }
}
