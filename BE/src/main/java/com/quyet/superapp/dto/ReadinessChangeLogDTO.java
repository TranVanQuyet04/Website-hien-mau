package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReadinessChangeLogDTO {
    private DonorReadinessLevel fromLevel;
    private DonorReadinessLevel toLevel;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime changedAt;
}
