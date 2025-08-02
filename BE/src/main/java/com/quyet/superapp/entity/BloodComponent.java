package com.quyet.superapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.quyet.superapp.enums.BloodComponentType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "BloodComponents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Blood_Component_ID")
    private Long bloodComponentId;

    @Column(name = "NameBloodComponent", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String name;

    @Column(name = "Code", columnDefinition = "VARCHAR(10)", nullable = false, unique = true)
    private String code; // PRC, FFP, PLT...

    @Column(name = "StorageTemp", columnDefinition = "VARCHAR(20)")
    private String storageTemp; // VD: "2-6°C"

    @Column(name = "StorageDays")
    private Integer storageDays;

    @Column(name = "Usage", columnDefinition = "NVARCHAR(200)")
    private String usage;

    @Column(name = "ApheresisCompatible")
    private Boolean isApheresisCompatible;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", columnDefinition = "VARCHAR(30)")
    private BloodComponentType type;

    /**
     * Thêm cột isActive để đánh dấu vô hiệu/khôi phục
     */
    @Builder.Default
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;

    // ❗ Quan hệ với các bảng khác
    @JsonIgnore
    @OneToMany(mappedBy = "component", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompatibilityRule> compatibilityRules;

    @JsonIgnore
    @OneToMany(mappedBy = "bloodComponent")
    private List<Donation> donations;

    @JsonIgnore
    @OneToMany(mappedBy = "component")
    private List<BloodUnit> bloodUnits;

    @JsonIgnore
    @OneToMany(mappedBy = "component")
    private List<BloodInventory> inventories;

    @JsonIgnore
    @OneToMany(mappedBy = "component")
    private List<BloodRequest> requests;
}
