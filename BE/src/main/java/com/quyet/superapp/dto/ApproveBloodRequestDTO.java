package com.quyet.superapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApproveBloodRequestDTO {
    private Long bloodRequestId; // ✅ đổi tên
    private String status;
    private Integer confirmedVolumeMl;
    private Boolean isUnmatched;
    private String emergencyNote;
    private String approvedBy;
}

