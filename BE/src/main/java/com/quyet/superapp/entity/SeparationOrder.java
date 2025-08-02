package com.quyet.superapp.entity;

import com.quyet.superapp.enums.SeparationMethod;
import com.quyet.superapp.enums.SeparationPattern;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "separation_order")
public class SeparationOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "separation_order_id")
    private Long separationOrderId;

    // 👉 Liên kết với túi máu gốc đã được hiến
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_bag_id", nullable = false)
    private BloodBag bloodBag;

    // 👤 Người thực hiện (có thể là userId, hoặc tên nhân viên)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    // ⏱️ Thời gian thao tác
    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    // ⚙️ Loại tách máu đã thực hiện
    @Enumerated(EnumType.STRING)
    @Column(name = "separation_method")
    private SeparationMethod separationMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "separation_pattern")
    private SeparationPattern separationPattern;

    // 📝 Ghi chú thêm (nếu có lỗi, đặc biệt, ... )
    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apheresis_machine_id")
    private ApheresisMachine machine;

    @Column(name = "PresetVersion")
    private String presetVersion;

}
