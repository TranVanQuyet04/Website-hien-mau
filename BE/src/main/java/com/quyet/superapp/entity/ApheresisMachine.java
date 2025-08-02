package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "apheresis_machine ")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApheresisMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apheresis_machine_id") // 👈 tên rõ ràng, đúng chuẩn hệ thống
    private Long apheresisMachineId;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "manufacturer", length = 50)
    private String manufacturer;

    @Column(name = "model", length = 50)
    private String model;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "last_maintenance")
    private LocalDate lastMaintenance;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    // (Tùy chọn) Quan hệ ngược với SeparationOrder
    @OneToMany(mappedBy = "machine")
    @ToString.Exclude
    private List<SeparationOrder> separationOrders;
}
