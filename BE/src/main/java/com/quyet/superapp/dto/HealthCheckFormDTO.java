package com.quyet.superapp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HealthCheckFormDTO {
    @NotNull(message = "ID không thể null")
    private Long id;

    @NotNull(message = "Registration ID không thể null")
    private Long registrationId;

    @DecimalMin(value = "30.0", message = "Nhiệt độ cơ thể phải ít nhất 30°C")
    @DecimalMax(value = "42.0", message = "Nhiệt độ cơ thể phải dưới 42°C")
    private Double bodyTemperature;

    @Min(value = 30, message = "Nhịp tim phải tối thiểu 30 lần/phút")
    @Max(value = 200, message = "Nhịp tim phải dưới 200 lần/phút")
    private Integer heartRate;

    @Min(value = 80, message = "Huyết áp tâm thu phải ít nhất 80")
    @Max(value = 180, message = "Huyết áp tâm thu phải dưới 180")
    private Integer bloodPressureSys;

    @Min(value = 50, message = "Huyết áp tâm trương phải ít nhất 50")
    @Max(value = 120, message = "Huyết áp tâm trương phải dưới 120")
    private Integer bloodPressureDia;

    @Min(value = 30, message = "Cân nặng phải ít nhất 30 kg")
    @Max(value = 200, message = "Cân nặng phải dưới 200 kg")
    private Double weightKg;
    private Double heightCm;
    private Boolean hasFever;
    private Boolean tookAntibioticsRecently;
    private Boolean hasChronicIllness;
    private Boolean isPregnantOrBreastfeeding;
    private Boolean hadRecentTattooOrSurgery;
    private Boolean hasRiskySexualBehavior;


    private Boolean isEligible;
    private String notesByStaff;

}
