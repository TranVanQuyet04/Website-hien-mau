package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodComponentFullDTO {

    private Long id;             // ID thành phần máu
    private String name;         // Tên thành phần máu (Hồng cầu, Huyết tương, ...)
    private String code;         // Mã viết tắt (PRC, FFP, ...)
    private String temperature;  // Nhiệt độ bảo quản (ví dụ: 2-6°C, -25°C)
    private int shelfLifeDays;   // Số ngày bảo quản (ví dụ: 42, 365)
    private Boolean isMachineSeparated; // Có tách máy được không (✔/✖)
    private String application;  // Ứng dụng lâm sàng (ví dụ: thiếu máu, sốc...)
}
