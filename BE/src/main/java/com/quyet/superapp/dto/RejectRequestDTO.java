package com.quyet.superapp.dto;

import lombok.Data;

@Data
public class RejectRequestDTO {
    private Long requestId;
    private String reason; // ✅ lý do từ chối
}
