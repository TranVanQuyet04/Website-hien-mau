package com.quyet.superapp.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.quyet.superapp.enums.BloodRequestStatus;

@Entity
@Table(name = "UrgentRequest")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrgentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UrgentRequest_Id")
    private Long urgentRequestId;

    @Column(name = "HospitalName", length = 100, nullable = false)
    private String hospitalName;

    @Column(name = "BloodType", length = 20, nullable = false)
    private String bloodType;

    @Column(name = "Units", nullable = false)
    private int units;

    @Column(name = "RequestDate", columnDefinition = "DATE")
    private LocalDate requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 20)
    private BloodRequestStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id")
    private User requester;

    @PrePersist
    public void setDefaultStatus() {
        if (status == null) {
            status = BloodRequestStatus.PENDING;
        }
        if (requestDate == null) {
            requestDate = LocalDate.now();
        }
    }
}
