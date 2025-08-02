package com.quyet.superapp.entity;

import com.quyet.superapp.entity.address.Address;
import com.quyet.superapp.enums.DonorReadinessLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "UrgentDonorRegistry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrgentDonorRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người dùng đăng ký hiến máu khẩn cấp
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User donor;

    // Nhóm máu của người hiến
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id", nullable = false)
    private BloodType bloodType;

    // Mức độ sẵn sàng hiến máu
    @Enumerated(EnumType.STRING)
    @Column(name = "readiness_level", length = 30, nullable = false)
    private DonorReadinessLevel readinessLevel; // ✅ Tên mới thay cho "mode"

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "last_contacted")
    private LocalDateTime lastContacted;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "left_group_at")
    private LocalDateTime leftGroupAt;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    // Tiện ích kiểm tra trạng thái
    public boolean isActiveAndVerified() {
        return Boolean.TRUE.equals(isAvailable) && Boolean.TRUE.equals(isVerified);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private BloodComponent bloodComponent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
