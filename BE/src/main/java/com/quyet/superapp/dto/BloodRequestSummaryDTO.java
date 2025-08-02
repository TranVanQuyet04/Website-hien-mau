package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyet.superapp.enums.BloodRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequestSummaryDTO {
    private Long bloodRequestId;
    private String patientName;
    private String patientBloodGroup;
    private String componentName;
    private Integer patientAge;
    private Integer quantityMl;
    private String triageLevel;
    private BloodRequestStatus status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
}
