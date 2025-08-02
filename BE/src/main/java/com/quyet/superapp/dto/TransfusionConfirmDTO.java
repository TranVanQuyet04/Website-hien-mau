package com.quyet.superapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransfusionConfirmDTO {
    private Long transfusionConfirmId;
    private String recipientName;
    private String bloodType;
    private int units;
    private LocalDateTime confirmedAt;
    private String status;
}
