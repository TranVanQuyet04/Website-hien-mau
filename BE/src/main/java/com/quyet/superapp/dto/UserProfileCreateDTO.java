package com.quyet.superapp.dto;

import lombok.Data;

@Data
public class UserProfileCreateDTO {
    private String fullName;
    private ContactInfoDTO contactInfo;
    private String citizenId;
    private String staffPosition; // ✅ dùng để set là "Doctor"
}
