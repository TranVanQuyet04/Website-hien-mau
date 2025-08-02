package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodBagStatus;
import com.quyet.superapp.enums.TestStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateBloodBagRequest {
    @Min(100)
    @Max(1000)
    private Integer volume; // thể tích mới (nếu cần)
    private Double hematocrit; // cập nhật Hct nếu có
    private TestStatus testStatus; // ví dụ cập nhật sau khi xét nghiệm
    private BloodBagStatus status; // ví dụ: USED, EXPIRED...
    private String note; // ghi chú mới (nếu cần)
    private Long bloodType;
    private String rh;
}
