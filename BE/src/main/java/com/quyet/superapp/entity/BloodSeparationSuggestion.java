package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_separation_suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodSeparationSuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BloodSeparationSuggestionID")
    private Long bloodSeparationSuggestionId;

    @ManyToOne
    @JoinColumn(name = "blood_bag_id")
    private BloodBag bloodBag;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_order_id", nullable = false, unique = true)
    private SeparationOrder separationOrder;

    @Column(name = "red_cells")
    private Integer redCellsMl;

    @Column(name = "plasma")
    private Integer plasmaMl;

    @Column(name = "platelets")
    private Integer plateletsMl;

    @Column(name = "red_cells_code")
    private String redCellsCode;

    @Column(name = "plasma_code")
    private String plasmaCode;

    @Column(name = "platelets_code")
    private String plateletsCode;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "suggested_by_user_id")
    private User suggestedBy;

    @Column(name = "suggested_at")
    private LocalDateTime suggestedAt;
}
