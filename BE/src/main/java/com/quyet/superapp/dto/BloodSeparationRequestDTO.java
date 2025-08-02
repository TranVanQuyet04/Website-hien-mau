package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationRequestDTO {
    private Long donationId;
    private int redCellsMl;
    private int plasmaMl;
    private int plateletsMl;
    private String method;
    private boolean leukoreduced;
    private Long staffId; // ID của nhân viên đang thực hiện
}
