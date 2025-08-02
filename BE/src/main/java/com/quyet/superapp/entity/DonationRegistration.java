package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DonationRegistrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Registration_Id")
    private Long registrationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User user;

    @Column(name = "ready_date")
    private LocalDateTime readyDate;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "slot_id")
    private Long slotId;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 20)
    private DonationStatus status;

    @Column(name = "location", columnDefinition = "NVARCHAR(100)")
    private String location;

    @Column(name = "blood_type", columnDefinition = "NVARCHAR(20)")
    private String bloodType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    @Column(name = "is_emergency")
    private Boolean isEmergency;
}
