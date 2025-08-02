package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Donations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Donation {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Donation_Id")
        private Long donationId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "User_Id")
        private User user; // ❓ Optional nếu đã có trong registration

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "registration_id")
        private DonationRegistration registration;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "blood_type", referencedColumnName = "BloodTypeID")
        private BloodType bloodType;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "component_id")
        private BloodComponent bloodComponent; // ✅ Gợi ý đặt lại tên


        @Column(name = "donation_time")
        private LocalDateTime donationDate; // ✅ Thay vì chỉ có ngày

        @Column(name = "volume_ml")
        private Integer volumeMl;

        @Column(name = "location", columnDefinition = "NVARCHAR(50)")
        private String location;

        @Column(name = "notes", columnDefinition = "NVARCHAR(200)")
        private String notes;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        @Enumerated(EnumType.STRING)
        @Column(name = "status", columnDefinition = "NVARCHAR(20)")
        private DonationStatus status;


        @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
        private List<BloodUnit> bloodUnits;

        @PrePersist
        protected void onCreate() {
                this.createdAt = LocalDateTime.now();
                this.updatedAt = LocalDateTime.now();
                if (this.donationDate == null) {
                        this.donationDate = LocalDateTime.now();
                }
        }

        @PreUpdate
        protected void onUpdate() {
                this.updatedAt = LocalDateTime.now();
        }

        @Column(name = "recovered_at")
        private LocalDate recoveryDate;

        @Column(name = "is_emergency")
        private Boolean isEmergency;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "separated_component_id")
        private BloodComponent separatedComponent; // 🧪 Thành phần máu thực tế được tách ra

}
