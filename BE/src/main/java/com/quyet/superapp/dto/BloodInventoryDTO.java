package com.quyet.superapp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodInventoryDTO {
    private Long bloodInventoryId;
    private Long bloodTypeId;
    private String bloodTypeName;
    private Long componentId;
    private String componentName;
    private Integer totalQuantityMl;
    private Integer standardBagSize;    // ✅ size 250ml, 350ml, 450ml
    private Integer estimatedBags;      // ✅ tính từ totalQuantityMl / standardBagSize
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


