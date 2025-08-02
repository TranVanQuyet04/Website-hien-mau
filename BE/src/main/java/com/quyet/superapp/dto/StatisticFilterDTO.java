package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticFilterDTO {

    @JsonFormat(pattern = "yyyy-MM-dd") // ✅ bắt buộc thêm
    private LocalDate fromDate;

    @JsonFormat(pattern = "yyyy-MM-dd") // ✅ bắt buộc thêm
    private LocalDate toDate;

    private Long bloodTypeId;
    private Long componentId;
    private String readinessLevel; // EMERGENCY_NOW, FLEXIBLE, REGULAR
}
