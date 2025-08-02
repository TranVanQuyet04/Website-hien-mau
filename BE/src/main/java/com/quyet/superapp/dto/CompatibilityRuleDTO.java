package com.quyet.superapp.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityRuleDTO {
    private Long compatibilityRuleId;
    private Long donorTypeId;
    private Long recipientTypeId;
    private Long componentId;
    private Boolean isCompatible;
}

