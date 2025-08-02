package com.quyet.superapp.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBloodBagRequest {
    @NotBlank
    private String bagCode;

    @NotNull
    private Long bloodTypeId; // ID của nhóm máu đã có sẵn trong bảng `blood_types`

    @NotBlank
    private String rh;

    @NotNull
    @Min(100)
    @Max(1000)
    private Integer volume;
    private Double hematocrit; // optional
    @NotNull
    private LocalDateTime collectedAt;
    @NotBlank
    private String donorId;

    private String note;
}
