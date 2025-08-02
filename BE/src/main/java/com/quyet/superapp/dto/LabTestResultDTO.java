package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabTestResultDTO {
    private Long labTestResultId;

    private Long bloodUnitId;

    private boolean hivNegative;
    private boolean hbvNegative;
    private boolean hcvNegative;
    private boolean syphilisNegative;
    private boolean malariaNegative;

    private boolean passed;

    private LocalDateTime testedAt;

    private Long testedById;
    private String testedByName; // Optional: Hiển thị tên nhân viên xét nghiệm
}
