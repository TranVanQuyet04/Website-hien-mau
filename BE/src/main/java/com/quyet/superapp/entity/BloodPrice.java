package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BloodPrices", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"blood_type_id", "component_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_type_id", nullable = false)
    private BloodType bloodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private BloodComponent bloodComponent;

    @Column(nullable = false)
    private Double pricePerBag;

    @Column
    private Double pricePerMl; // optional, nếu tính theo ml
}

