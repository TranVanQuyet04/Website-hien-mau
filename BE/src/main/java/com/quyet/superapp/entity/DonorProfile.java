package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quyet.superapp.enums.DonorReadinessLevel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DonorProfiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "readiness_level", length = 30)
    private DonorReadinessLevel readinessLevel;

    @Column(name = "note", columnDefinition = "NVARCHAR(255)")
    private String note;

    public DonorProfile(User user) {
        this.user = user;
    }

    public String getBloodTypeName() {
        if (user != null && user.getUserProfile() != null && user.getUserProfile().getBloodType() != null) {
            return user.getUserProfile().getBloodType().getDescription();
        }
        return null;
    }

    public String getFullName() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getFullName()
                : null;
    }

    public String getPhone() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getPhone()
                : null;
    }

    public String getEmail() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getEmail()
                : null;
    }

    public String getDob() {
        return user != null && user.getUserProfile() != null && user.getUserProfile().getDob() != null
                ? user.getUserProfile().getDob().toString()
                : null;
    }

    public String getGender() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getGender()
                : null;
    }

    public String getCitizenId() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getCitizenId()
                : null;
    }

    public String getLocation() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getLocation()
                : null;
    }

    public String getAddressText() {
        return user != null && user.getUserProfile() != null && user.getUserProfile().getAddress() != null
                ? user.getUserProfile().getAddress().toString()
                : null;
    }

    public Double getHeight() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getHeight()
                : null;
    }

    public Double getWeight() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getWeight()
                : null;
    }

    public String getOccupation() {
        return user != null && user.getUserProfile() != null
                ? user.getUserProfile().getOccupation()
                : null;
    }

    public String getLastDonationDate() {
        return user != null && user.getUserProfile() != null && user.getUserProfile().getLastDonationDate() != null
                ? user.getUserProfile().getLastDonationDate().toLocalDate().toString()
                : "--";
    }

    public String getRecoveryTime() {
        return user != null && user.getUserProfile() != null && user.getUserProfile().getRecoveryTime() != null
                ? user.getUserProfile().getRecoveryTime() + " ngày"
                : "--";
    }

    public boolean isVerified() {
        return readinessLevel == DonorReadinessLevel.EMERGENCY_NOW || readinessLevel == DonorReadinessLevel.EMERGENCY_FLEXIBLE;
    }
}
