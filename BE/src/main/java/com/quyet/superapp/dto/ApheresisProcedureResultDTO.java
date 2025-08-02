package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApheresisProcedureResultDTO {
    private Long separationOrderId;
    private String operator;
    private String machineSerial;
    private LocalDateTime performedAt;

    private Integer redCells;
    private Integer plasma;
    private Integer platelets;

    private String unitCodesCombined;
    private String note;
}
