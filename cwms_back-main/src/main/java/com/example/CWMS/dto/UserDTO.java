package com.example.CWMS.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private int Id;
    private String UserName;
    private String Email;
    private String PasswordHash;
    private String FirstName;
    private String LastName;
    private  int IsActive;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
    private String roleName;
    private String siteName;

}
