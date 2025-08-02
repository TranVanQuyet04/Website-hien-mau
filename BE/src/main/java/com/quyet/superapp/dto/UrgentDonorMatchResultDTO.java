package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrgentDonorMatchResultDTO {
    private Long id;
    private String fullName;
    private String phone;
    private String address;
    private String bloodType;
    private String component;
    private Double distance;

    private String readiness;
    private Boolean verified;
    private Boolean compatible;
    private Boolean canDonateNow;
    private Integer daysUntilRecover;
}
