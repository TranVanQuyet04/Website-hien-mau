package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BloodGroupDistributionDTO {
    private String bloodType;
    private long quantity; // tổng lượng máu ml còn lại
}

