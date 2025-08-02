package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifiedUrgentDonorDTO {
    private Long userId;
    private String fullName;
    private String bloodType;
    private String phone;
    private String location;
    private String addressFull;
    // ✅ Mới thêm
    private DonorReadinessLevel readinessLevel;
    private String readinessDescription; // VD: "Có thể hiến ngay"
}
