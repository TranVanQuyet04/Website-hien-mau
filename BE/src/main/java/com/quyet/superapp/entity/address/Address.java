package com.quyet.superapp.entity.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "name_street", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String addressStreet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    @JsonIgnore
    private Ward ward;

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.addressStreet);

        if (ward != null) {
            sb.append(", ").append(ward.getWardName());
            if (ward.getDistrict() != null) {
                sb.append(", ").append(ward.getDistrict().getDistrictName());
                if (ward.getDistrict().getCity() != null) {
                    sb.append(", ").append(ward.getDistrict().getCity().getNameCity());
                }
            }
        }

        return sb.toString();
    }

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;


}
