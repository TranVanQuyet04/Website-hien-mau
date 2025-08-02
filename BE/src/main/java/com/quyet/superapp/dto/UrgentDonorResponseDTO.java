package com.quyet.superapp.dto;

import com.quyet.superapp.entity.address.Address;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UrgentDonorResponseDTO {
    private Long userId;
    private String fullName;
    private String bloodType;
    private String location;
    private String phone;
    private Address address;

}
