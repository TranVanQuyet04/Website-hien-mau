package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationResultDTO {
    private Long separationId;
    private int redCells;
    private int plasma;
    private int platelets;
    private String performedBy;
    private LocalDateTime separatedAt;
}
