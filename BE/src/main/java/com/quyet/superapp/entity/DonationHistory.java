package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "DonationHistories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id") // 👈 Chỉ định rõ tên cột
    private User donor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id") // 👈 Chỉ định rõ tên cột
    private Donation donation;


    private LocalDateTime donatedAt;

    private String bloodType;

    private String componentDonated;

    private Integer volumeMl;

    private String donationLocation;

    private String status; // COMPLETED / REJECTED / PENDING

    // 🔐 Phần thanh toán
    private Boolean paymentRequired;

    private Boolean paymentCompleted;

    private Integer paymentAmount;

    private LocalDateTime paymentTime;

    private String paymentMethod; // VNPAY / CASH / BANK_TRANSFER

    private String transactionCode;

    private LocalDateTime createdAt;

}


