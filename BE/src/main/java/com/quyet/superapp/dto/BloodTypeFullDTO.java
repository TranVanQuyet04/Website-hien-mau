package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodTypeFullDTO {
    private Long id;
    private String bloodGroup; // A, B, AB, O
    private String rh;         // +/-
    private String note;       // Phổ biến, Toàn năng
    private Boolean isActive;  // true/false
}
