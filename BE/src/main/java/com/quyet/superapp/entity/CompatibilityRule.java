package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CompatibilityRules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Rule_Id")
    private Long compatibilityRuleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_type", referencedColumnName = "BloodTypeID")
    private BloodType donorType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_type", referencedColumnName = "BloodTypeID")
    private BloodType recipientType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private BloodComponent component;

    @Column(name = "is_compatible")
    private Boolean isCompatible;
}
