package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HealthCheckForms")
@Builder
public class HealthCheckForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_check_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false)
    private DonationRegistration registration;

    //  Thông số sinh tồn
    @Column(name = "body_temperature")
    private Double bodyTemperature;  // Nhiệt độ cơ thể (°C)

    @Column(name = "heart_rate")
    private Integer heartRate;  // Mạch (lần/phút)

    @Column(name = "blood_pressure_sys")
    private Integer bloodPressureSys;  // Huyết áp tâm thu

    @Column(name = "blood_pressure_dia")
    private Integer bloodPressureDia;   // Huyết áp tâm trương

    @Column(name = "height_cm")
    private Double heightCm; // Chiều cao (cm)

    @Column(name = "weight_kg")
    private Double weightKg;     // Cân nặng (kg)

    // 🚫 Câu hỏi loại trừ
    @Column(name = "has_fever")
    private Boolean hasFever;

    @Column(name = "took_antibiotics_recently")
    private Boolean tookAntibioticsRecently;

    @Column(name = "has_chronic_illness")
    private Boolean hasChronicIllness;

    @Column(name = "is_pregnant_or_breastfeeding")
    private Boolean isPregnantOrBreastfeeding;

    @Column(name = "had_recent_tattoo_or_surgery")
    private Boolean hadRecentTattooOrSurgery;

    @Column(name = "has_risky_sexual_behavior")
    private Boolean hasRiskySexualBehavior;

    // ✅ Kết quả cuối cùng
    @Column(name = "is_eligible")
    private Boolean isEligible;

    @Column(name = "notes_by_staff", length = 500)
    private String notesByStaff;

    @ManyToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;
}
