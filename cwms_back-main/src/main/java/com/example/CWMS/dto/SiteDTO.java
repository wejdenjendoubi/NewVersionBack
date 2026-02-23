package com.example.CWMS.dto;

import java.util.Date;

public class SiteDTO {
    private int id;
    private String nameSite;
    private Date createdAt;
    private Date updatedAt;

    // Constructeurs
    public SiteDTO() {}

    public SiteDTO(int id, String nameSite, Date createdAt, Date updatedAt) {
        this.id = id;
        this.nameSite = nameSite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNameSite() { return nameSite; }
    public void setNameSite(String nameSite) { this.nameSite = nameSite; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}