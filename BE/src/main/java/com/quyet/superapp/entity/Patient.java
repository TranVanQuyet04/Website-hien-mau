package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "Patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String phone;
    private String gender;
    private Integer age;
    private Double weight;

    private String bloodGroup;
    private String citizenId;

    @Column(name = "Insurance_Card_Number", unique = true, length = 50)
    private String insuranceCardNumber;

    @Column(name = "Insurance_Valid_To")
    private LocalDateTime insuranceValidTo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_user_id")
    private User linkedUser;

    private LocalDateTime createdAt;
}
