package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Report_Id")
    private Long reportId;

    @Column(name = "report_type", columnDefinition = "NVARCHAR(20)", nullable = false)
    private String reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generated_by")
    private User generatedBy;

    @Column(name = "created_at", columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @Column(name = "content", columnDefinition = "NVARCHAR(200)")
    private String content;
}
