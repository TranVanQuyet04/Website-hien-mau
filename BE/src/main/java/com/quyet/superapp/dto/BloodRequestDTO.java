package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // ✅ Thêm dòng này
public class BloodRequestDTO {

    private Long bloodRequestId;

    private String patientRecordCode;

    // === Người yêu cầu
    private String requesterName;
    private String requesterPhone;


    // Bác sĩ phụ trách
    private String doctorName;
    private String doctorPhone;

    // === Bệnh nhân
    private String patientName;
    private String patientPhone;
    private Integer patientAge;
    private String patientGender;
    private Double patientWeight;
    private String patientBloodGroup;

    private Long suspectedPatientId;

    // === Truyền máu
    private String bloodTypeName;       // ✅ MỚI: Hiển thị tên loại máu

    private String componentName;       // ✅ MỚI: Hiển thị tên thành phần máu

    private Integer quantityBag;
    private Integer quantityMl;
    private String urgencyLevel;
    private String triageLevel;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime neededAt;

    // === Lịch sử y khoa
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    // === Ghi chú & cảnh báo
    private String warningNote;
    private String specialNote;

    // === Metadata & xác nhận
    private String status;
    private Integer confirmedVolumeMl;
    private Boolean isUnmatched;
    private Long codeRedId;
    private String emergencyNote;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    private Integer totalAmount;
    private String paymentStatus;
    private Boolean deferredPayment;
    private String deferredPaymentReason;
    private String cancelReason;

    private String requestCode;


}
