package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BloodInventoryViewDTO {
    private Long id;
    private String bloodType;
    private String componentName;
    private Integer quantity;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
