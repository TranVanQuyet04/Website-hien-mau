package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationSuggestionDTO {
    private int redCellsMl;
    private int plasmaMl;
    private int plateletsMl;
    private String redCellLabel;
    private String plasmaLabel;
    private String plateletsLabel;
    private String note;
}
