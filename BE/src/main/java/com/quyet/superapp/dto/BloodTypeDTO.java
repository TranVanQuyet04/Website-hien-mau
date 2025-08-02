package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodTypeDTO {
    private Long bloodTypeId;

    @NotBlank(message = "Mô tả nhóm máu không được để trống")
    private String description; // ví dụ: "O+"

    private String rh;          // "+" hoặc "-"
    private String note;        // Ghi chú (phổ biến, toàn năng...)
    private Boolean isActive;   // true/false
}
