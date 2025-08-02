package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrgentDonorDetailDTO {
    private String fullName;
    private String dateOfBirth;
    private String gender;
    private String citizenId;
    private String phone;
    private String email;
    private String address;
    private String bloodType;
    private DonorReadinessLevel readinessLevel;
    private String readinessDescription;
    private String lastDonationDate;
    private String recoveryTime;
    private String occupation;
    private String status;
}
