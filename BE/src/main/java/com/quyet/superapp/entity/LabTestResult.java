package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_test_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LabTestResultID")
    private Long labTestResultId;

    // Mỗi đơn vị máu chỉ có 1 kết quả xét nghiệm
    @OneToOne
    @JoinColumn(name = "BloodUnitID", unique = true)
    private BloodUnit bloodUnit;

    @Column(name = "HIV_Negative")
    private boolean hivNegative;

    @Column(name = "HBV_Negative")
    private boolean hbvNegative;

    @Column(name = "HCV_Negative")
    private boolean hcvNegative;

    @Column(name = "Syphilis_Negative")
    private boolean syphilisNegative;

    @Column(name = "Malaria_Negative")
    private boolean malariaNegative;

    @Column(name = "Passed")
    private boolean passed; // tổng hợp kết quả

    @Column(name = "TestedAt")
    private LocalDateTime testedAt;

    @ManyToOne
    @JoinColumn(name = "TestedByUserID")
    private User testedBy;
}
