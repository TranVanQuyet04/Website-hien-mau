package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.TestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_bags")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodBag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blood_bag_id") // 👈 đặt tên rõ ràng
    private Long bloodBagId;

    @Column(name = "bag_code", nullable = false, unique = true, length = 20)
    private String bagCode;

    @ManyToOne
    @JoinColumn(name = "blood_type_id")
    private BloodType bloodType;

    @Column(name = "rh", nullable = false, length = 2)
    private String rh;

    @Column(name = "volume", nullable = false)
    private Integer volume;

    @Column(name = "hematocrit")
    private Double hematocrit;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "test_status", nullable = false)
    private TestStatus testStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BloodBagStatus status;

    @Column(name = "donor_id", nullable = false)
    private String donorId;

    @Column(name = "note")
    private String note;
    @OneToOne
    @JoinColumn(name = "registration_id")
    private DonationRegistration registration;


}
