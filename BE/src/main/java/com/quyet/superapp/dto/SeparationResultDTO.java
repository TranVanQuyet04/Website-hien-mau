package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationResultDTO {
    private Long separationOrderId;
    private BloodSeparationSuggestionDTO suggestion;
    private List<BloodUnitDTO> createdUnits;
    private String note;

    // ✅ Thêm 2 trường mới
    private String bagCode;
    private String status;
}
