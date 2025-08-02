package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class CurrentUrgentDonorStatusDTO {
    private DonorReadinessLevel mode;

    @JsonFormat(pattern = "dd-MM-yyyy") // ✅ định dạng ngày sinh
    private LocalDateTime registeredAt;

    @JsonFormat(pattern = "dd-MM-yyyy") // ✅ định dạng ngày sinh
    private Boolean isVerified;
    private LocalDateTime leftGroupAt;
}
