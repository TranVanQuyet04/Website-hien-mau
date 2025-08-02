package com.quyet.superapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodRequestConfirmDTO {
    private Long requestId;
    private Integer confirmedVolumeMl; // ✅ lượng máu staff thực tế nhận
}
