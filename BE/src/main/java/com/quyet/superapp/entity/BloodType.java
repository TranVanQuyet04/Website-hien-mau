package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "BloodTypes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodTypeID")
    private Long bloodTypeId;

    @Column(name = "Description", columnDefinition = "NVARCHAR(20)", nullable = false)
    private String description; // A, B, AB, O

    @Column(name = "Rh", columnDefinition = "VARCHAR(10)", nullable = false)
    private String rh; // "+" hoặc "-"

    @Column(name = "Note", columnDefinition = "NVARCHAR(100)")
    private String note; // ví dụ: "Phổ biến", "Rất hiếm", "Toàn năng"

    @Column(name = "IsActive", nullable = false, columnDefinition = "BIT DEFAULT 1")
    private Boolean isActive = true;

    @Column(name = "Code", columnDefinition = "VARCHAR(10)", nullable = false, unique = true)
    private String code; // A+, B-, AB+, O- ...

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Donation> donations;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodUnit> bloodUnits;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodInventory> bloodInventories;

    @OneToMany(mappedBy = "bloodType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BloodRequest> bloodRequests;

    @OneToMany(mappedBy = "donorType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompatibilityRule> donorRules;

    @OneToMany(mappedBy = "recipientType", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CompatibilityRule> recipientRules;

    @PrePersist
    @PreUpdate
    public void generateCode() {
        if (description != null && rh != null) {
            this.code = (description + rh).toUpperCase().replaceAll("\\s+", "");
        }
    }
    @Override
    public String toString() {
        return code;
    }

}
