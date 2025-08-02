package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearbyDonorDTO {
    private Long userId;
    private String fullName;
    private String bloodType;
    private String readinessLevel;

    private double distanceKm;
    private String nearestAddress;
    private String phoneNumber;

    private LocalDate lastDonationDate;
    private LocalDate nextAvailableDate;
    private String lastComponentDonated;

    private boolean isEligibleToDonate;
    private String recoveryStatus; // Ví dụ: "✅ Đủ điều kiện", "❌ Còn 3 ngày nữa"
}



