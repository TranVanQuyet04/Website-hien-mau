package com.quyet.superapp.dto;

import com.quyet.superapp.entity.UrgentDonorRegistry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DonorWithDistance {
    private UrgentDonorRegistry donor;
    private double distanceKm;
}

