package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodSeparationDetailDTO {
    private Long id;
    private Long separationLogId;
    private Long bloodTypeId;
    private Long componentId;
    private Integer volumeMl;
    private String unitCode;
    private LocalDateTime createdAt;

}
