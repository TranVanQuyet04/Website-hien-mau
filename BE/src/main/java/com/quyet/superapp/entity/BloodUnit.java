package com.quyet.superapp.entity;

import com.quyet.superapp.enums.BloodUnitStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "BloodUnits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Long bloodUnitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BloodType", referencedColumnName = "BloodTypeID")
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private BloodComponent component;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_bag_id")
    private BloodBag bloodBag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_order_id")
    private SeparationOrder separationOrder;

    @Column(name = "unit_code", unique = true, length = 20)
    private String unitCode;

    @Column(name = "quantity_ml")
    private Integer quantityMl;

    @Column(name = "expiration_date", columnDefinition = "DATE")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(20)")
    private BloodUnitStatus status;

    @Column(name = "stored_at", columnDefinition = "DATETIME")
    private LocalDateTime storedAt;

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
    @ManyToOne
    @JoinColumn(name = "donation_id") // tên cột foreign key trong bảng blood_unit
    private Donation donation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private BloodInventory inventory;

    public LocalDate getExpiryDate() {
        return this.expirationDate;
    }

    @Override
    public String toString() {
        return "BloodUnit{" +
                "id=" + bloodUnitId +
                ", code='" + unitCode + '\'' +
                ", bloodType=" + (bloodType != null ? bloodType.getDescription() : "null") +
                ", component=" + (component != null ? component.getName() : "null") +
                ", status=" + status +
                ", exp=" + expirationDate +
                '}';
    }




}
