package com.quyet.superapp.entity.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "Ward",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"ward_name", "district_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ward_id")
    private Long wardId;

    @Column(name = "ward_name", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String wardName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    @JsonBackReference // ✅ Tránh vòng lặp ngược
    private District district;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

}
