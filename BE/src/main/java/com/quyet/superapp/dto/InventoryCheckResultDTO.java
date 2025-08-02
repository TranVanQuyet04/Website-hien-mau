package com.quyet.superapp.dto;


import com.quyet.superapp.enums.InventoryCheckResultStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCheckResultDTO {
    private boolean canFulfill;            // Có thể đáp ứng hay không
    private int quantityRequested;
    private int quantityAvailable;
    private LocalDate nearestExpiryDate;
    private List<String> fallbackBloodGroups; // Ví dụ: ["O+", "O-"]
    private String message;               // Mô tả ngắn
    private InventoryCheckResultStatus status; // ✅ ENUM mã kết quả
}

