package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UrgentDonorSearchResultDTO {
    private Long id;
    private String fullName;
    private String phone;     // có thể ẩn một phần ở UI
    private String address;
    private String bloodType;
    private String component;
    private Double distance;  // km
}
