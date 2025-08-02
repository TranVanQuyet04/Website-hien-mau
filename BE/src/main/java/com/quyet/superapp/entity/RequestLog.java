package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "RequestLogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BloodRequestId")
    private BloodRequest bloodRequest;

    @Column(name = "Action", length = 50)
    private String action; // VD: "STAFF_CREATED", "ADMIN_APPROVED", etc.

    @Column(name = "PerformedBy", length = 100)
    private String performedBy;

    @Column(name = "Timestamp")
    private LocalDateTime timestamp;

    @Column(name = "Note", columnDefinition = "NVARCHAR(500)")
    private String note;
}
