package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadinessConflictCheckDTO {
    private boolean hasConflict;
    private String currentMode; // có thể null
}
