package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodUnitDTO {
    private Long bloodUnitId;
    private Long bloodTypeId;
    private Long componentId;
    private Long bloodBagId; // ✅ đổi tên cho rõ nghĩa
    private Integer quantityMl;
    private LocalDate expirationDate;
    private String status;
    private LocalDateTime storedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String unitCode;         // để truy xuất hoặc in tem
    private String bloodTypeName;    // để hiển thị rõ hơn
    private String componentName;    // để hiển thị rõ hơn
}


