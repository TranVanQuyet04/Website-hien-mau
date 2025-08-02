package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transfusions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transfusion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transfusion_Id")
    private Long transfusionId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private BloodRequest request;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blood_unit_id")
    private BloodUnit bloodUnit;

    @Column(name = "transfusion_date", columnDefinition = "DATETIME")
    private LocalDateTime transfusionDate;

    @Column(name = "status", columnDefinition = "NVARCHAR(20)")
    private String status;

    @Column(name = "notes", columnDefinition = "NVARCHAR(200)")
    private String notes;

    @Column(name = "volume_taken_ml")
    private Integer volumeTakenMl;

    @Column(name = "recipient_name", columnDefinition = "NVARCHAR(100)")
    private String recipientName;

    @Column(name = "recipient_phone", columnDefinition = "NVARCHAR(20)")
    private String recipientPhone;

    @Column(name = "approved_by", columnDefinition = "NVARCHAR(50)")
    private String approvedBy;

}
