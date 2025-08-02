package com.quyet.superapp.entity;

import com.quyet.superapp.enums.DonorReadinessLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ReadinessChangeLog")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadinessChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User donor;  // ✅ tên đúng là donor

    @Enumerated(EnumType.STRING)
    @Column(name = "from_level")
    private DonorReadinessLevel fromLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_level")
    private DonorReadinessLevel toLevel;

    private LocalDateTime changedAt;
}

