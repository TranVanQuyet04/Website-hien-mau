package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import lombok.Data;

// dùng cho PUT /{id}/update
@Data
public class BloodComponentUpdateDTO {
    private String name;
    private String code;
    private String storageTemp;
    private Integer storageDays;
    private String usage;
    private Boolean isApheresisCompatible;
    private BloodComponentType type;
    private Boolean isActive;
}

