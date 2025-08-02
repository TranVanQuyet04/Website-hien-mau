package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "SeparationPresetConfig")
@AllArgsConstructor
@NoArgsConstructor
public class SeparationPresetConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gender", columnDefinition = "NVARCHAR(10)")
    private String gender; // male / female

    @Column(name = "min_weight")
    private int minWeight; // ngưỡng cân nặng từ bao nhiêu kg

    @Column(name = "method", columnDefinition = "NVARCHAR(20)")
    private String method; // ly tâm, gạn tách

    @Column(name = "leukoreduced")
    private boolean leukoreduced;

    @Column(name = "rbc_ratio")
    private double rbcRatio;

    @Column(name = "plasma_ratio")
    private double plasmaRatio;

    @Column(name = "platelets_fixed")
    private int plateletsFixed;

    @Column(name = "Version")
    private String version;

}
