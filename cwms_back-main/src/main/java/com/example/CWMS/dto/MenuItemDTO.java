package com.example.CWMS.dto;

import com.example.CWMS.model.MenuItem;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDTO {
    private Integer menuItemId;
    private String label;
    private String icon;
    private String link;
    private MenuItem parentId;
    private Boolean isTitle;
    private Boolean isLayout;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
