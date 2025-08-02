package com.quyet.superapp.dto;

import lombok.Data;

@Data
public class EmergencyDonorDTO {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String bloodType;
    private String location;
    private String mode;         // e.g., EMERGENCY_NOW
    private Boolean isAvailable;
}

