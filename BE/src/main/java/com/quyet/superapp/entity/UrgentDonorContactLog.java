package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "UrgentDonorContactLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrgentDonorContactLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_request_id")
    private BloodRequest bloodRequest;

    @Column(name = "contacted_at")
    private LocalDateTime contactedAt;

    @Column(name = "status", columnDefinition = "NVARCHAR(20)")
    private String status; // Ví dụ: PENDING, CONTACTED, CONFIRMED
}