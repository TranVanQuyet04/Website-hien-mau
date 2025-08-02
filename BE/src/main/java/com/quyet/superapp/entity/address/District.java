package com.quyet.superapp.entity.address;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(
        name = "District",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"district_name", "city_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "district_id")
    private Long districtId;

    @Column(name = "district_name", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String districtName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // ✅ Tránh vòng lặp: District → Ward → District
    private List<Ward> wards;
}
