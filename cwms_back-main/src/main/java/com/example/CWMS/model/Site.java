package com.example.CWMS.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Sites")
public class Site {
    @Id
    private int Id;

    @Column(name = "NameSite")
    private String NameSite;

    @Column(name="CreatedAt")
    private Date CreatedAt;

    @Column(name="UpdatedAt")
    private Date UpdatedAt;

    // Constructeur par défaut
    public Site() {}

    // --- GETTERS ET SETTERS ---

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getSiteName() {
        return NameSite;
    }

    public void setNameSite(String nameSite) {
        this.NameSite = nameSite;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.UpdatedAt = updatedAt;
    }
}