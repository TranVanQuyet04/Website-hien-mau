package com.quyet.superapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodComponentType {
    RED_BLOOD_CELLS(84, "Hồng cầu"),
    PLASMA(28, "Huyết tương"),
    PLATELETS(14, "Tiểu cầu"),
    WHOLE_BLOOD(84, "Toàn phần");

    private final int recoveryDays;
    private final String description;

    BloodComponentType(int recoveryDays, String description) {
        this.recoveryDays = recoveryDays;
        this.description = description;
    }

    public int getRecoveryDays() {
        return recoveryDays;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static BloodComponentType from(String value) {
        if (value == null) {
            return null;
        }
        String v = value.trim().toUpperCase();
        // Chuẩn hóa các dạng số ít sang số nhiều
        if ("PLATELET".equals(v)) {
            v = "PLATELETS";
        }
        for (BloodComponentType type : values()) {
            if (type.name().equals(v)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown BloodComponentType: " + value);
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
