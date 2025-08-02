package com.quyet.superapp.dto;

import lombok.Data;

@Data
public class AddressRequestDTO {
    private String addressStreet;
    private Long wardId;

    // ✅ Bổ sung tọa độ vị trí
    private Double latitude;
    private Double longitude;
}
