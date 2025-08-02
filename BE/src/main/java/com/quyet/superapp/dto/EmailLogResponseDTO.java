package com.quyet.superapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EmailLogResponseDTO {
    private Long id;
    private Long userId;
    private String recipientEmail;
    private String subject;
    private String type;
    private String status;
    private LocalDateTime sentAt;
}
