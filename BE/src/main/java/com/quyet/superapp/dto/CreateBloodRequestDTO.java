package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO dùng để tạo đơn yêu cầu truyền máu mới từ phía STAFF.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // ✅ Bỏ qua các trường không khai báo trong DTO (ví dụ: "status")
public class CreateBloodRequestDTO {

    private Long requesterId;           // Người gửi yêu cầu (staff)
    private String patientRecordCode;   // Mã hồ sơ bệnh án (nếu có)

    private Long doctorId;              // Bác sĩ phụ trách

    // ===== Thông tin bệnh nhân =====
    private String citizenId;
    private String patientName;
    private String patientPhone;
    private Integer patientAge;
    private String patientGender;
    private Double patientWeight;
    private String patientBloodGroup;

    // ===== Chi tiết truyền máu =====
    private Long bloodTypeId;
    private Long expectedBloodTypeId;
    private Long componentId;
    private Integer quantityBag;
    private Integer quantityMl;
    private String urgencyLevel;
    private String triageLevel;
    private String reason;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime neededAt; // Thời gian cần truyền máu

    // ===== Lịch sử y khoa =====
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    // ===== Cảnh báo & ghi chú =====
    private String warningNote;
    private String specialNote;

    // ===== Metadata khác =====
    private Boolean isUnmatched;
    private Long codeRedId;
    private Boolean isAnonymousPatient;
    private Long suspectedPatientId;
    private String emergencyNote;

    // ===== Thông tin thanh toán =====
    private Boolean deferredPayment;
    private String deferredPaymentReason;

    // ===== Ưu tiên nội bộ =====
    private String internalPriorityCode;

    // ===== Thông tin BHYT =====
    private Boolean hasInsurance;
    private String insuranceCardNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceValidTo;
}
