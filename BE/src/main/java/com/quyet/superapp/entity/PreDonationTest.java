package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PreDonationTests")
public class PreDonationTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pre_donation_test_id") // ✅ tên ID rõ ràng
    private Long preDonationTestId;

    @OneToOne
    @JoinColumn(name = "health_check_id", nullable = false)
    private HealthCheckForm healthCheckForm;


    private Boolean hivResult;
    private Boolean hbvResult;
    private Boolean hcvResult;
    private Boolean syphilisResult;

    private Double hbLevel;

    @ManyToOne
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    private LocalDate testDate;

}
