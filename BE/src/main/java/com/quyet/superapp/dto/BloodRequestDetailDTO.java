package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BloodRequestDetailDTO {
    private Long bloodRequestId;

    // Người yêu cầu
    private String requesterName;
    private String requesterPhone;
    private String requesterPosition;
    private String requesterDepartment;

    // Bệnh nhân
    private String patientName;
    private String patientPhone;
    private Integer patientAge;
    private String patientGender;
    private Double patientWeight;
    private String patientBloodGroup;

    // ...
    // Các trường còn lại tương ứng với entity để hiển thị chi tiết
}

