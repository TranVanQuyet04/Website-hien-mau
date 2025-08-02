package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrgentDonorSearchRequestDTO {
    private Long receiverBloodTypeId; // ID của nhóm máu người nhận
    private Long componentId;         // ID của thành phần máu cần
    private Double radiusKm = 10.0;   // Bán kính tìm kiếm (km), mặc định 10km
}

