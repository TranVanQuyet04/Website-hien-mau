package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BloodRequestWithNewPatientDTO {

    // 🔎 Tra cứu bệnh nhân
    private String citizenId;
    @JsonProperty("patientId")
    private Long patientId;  // alias cho suspectedPatientId


    // 👤 Thông tin bệnh nhân
    @NotBlank
    private String patientName;

    @NotBlank
    private String patientPhone;

    @NotNull
    private Integer patientAge;

    @NotBlank
    private String patientGender;

    @NotNull
    private Double patientWeight;

    @NotBlank
    private String patientBloodGroup;

    // 🧑‍⚕️ Thông tin người gửi yêu cầu và bác sĩ phụ trách
    @NotNull
    private Long requesterId;

    @NotNull
    private Long doctorId;

    // 🩸 Yêu cầu truyền máu
    @NotBlank
    private String reason;

    @NotBlank
    private String urgencyLevel;

    @NotBlank
    private String triageLevel;

    @NotNull
    private Integer quantityBag;

    @NotNull
    private Integer quantityMl;

    @NotNull
    private Long bloodTypeId;

    @NotNull
    private Long componentId;

    private Long expectedBloodTypeId;
    private String priorityCode;

    // ⏰ Thời gian cần truyền máu
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime neededAt;

    // 🧪 Lịch sử y khoa
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    // ⚠️ Ghi chú
    private String warningNote;
    private String specialNote;

    // 🩺 Mã bệnh án
    private String patientRecordCode;

    // 💰 Thanh toán
    private Boolean deferredPayment;
    private String deferredPaymentReason;

    // 🩹 BHYT
    private Boolean hasInsurance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceValidTo;

    private String insuranceCardNumber;
}
