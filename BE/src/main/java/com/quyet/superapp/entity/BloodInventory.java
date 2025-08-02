package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodInventoryStatus;
import com.quyet.superapp.enums.BloodUnitStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "BloodInventory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodInventoryID")
    private Long bloodInventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BloodType", referencedColumnName = "BloodTypeID")
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ComponentID")
    private BloodComponent component;

    @Column(name = "TotalQuantityML")
    private Integer totalQuantityMl;

    @Column(name = "LastUpdated", columnDefinition = "DATETIME")
    private LocalDateTime lastUpdated;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ✅ Thêm mã túi máu liên kết (nếu không dùng join với BloodBag)
    @Column(name = "bag_code")
    private String bagCode;

    // ✅ Thêm trạng thái tồn kho (ví dụ: IN_STOCK, LOW, EXPIRED...)
    @Column(name = "status", length = 50)
    private String status;


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Column(name = "MinThresholdML")
    private Integer minThresholdMl;

    @Column(name = "CriticalThresholdML")
    private Integer criticalThresholdMl;


    @Column(name = "ExpiredAt")
    private LocalDateTime expiredAt;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private List<BloodUnit> bloodUnits;

    public int getAvailableBagCount() {
        if (bloodUnits == null) return 0;
        return (int) bloodUnits.stream()
                .filter(unit -> unit.getStatus() == BloodUnitStatus.STORED &&
                        (unit.getExpiryDate() == null || unit.getExpiryDate().isAfter(LocalDate.now())))
                .count();
    }



}
