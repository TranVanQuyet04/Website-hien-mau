package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "BloodComponentPricing")
@Data // ✅ Thêm để sinh getter/setter
public class BloodComponentPricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id", nullable = false)
    private BloodComponent component;

    @Column(nullable = false)
    private Integer unitPrice; // Giá mỗi túi

    @Column(nullable = false)
    private LocalDate effectiveDate; // Ngày áp dụng
}
