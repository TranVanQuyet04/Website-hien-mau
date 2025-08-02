package com.quyet.superapp.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class BloodRequestWithExistingPatientDTO {

    // 👤 Bệnh nhân đã tồn tại
    @NotNull(message = "ID bệnh nhân là bắt buộc")
    private Long patientId;

    // 🧑‍⚕️ Người yêu cầu & bác sĩ
    @NotNull(message = "Người gửi yêu cầu là bắt buộc")
    private Long requesterId;

    @NotNull(message = "Bác sĩ phụ trách là bắt buộc")
    private Long doctorId;

    // 🩸 Nhóm máu yêu cầu
    @NotNull(message = "Nhóm máu cần truyền là bắt buộc")
    private Long bloodTypeId;

    // 🆗 Nhóm máu có thể thay thế (tùy chọn)
    private Long expectedBloodTypeId;

    @NotNull(message = "Thành phần máu là bắt buộc")
    private Long componentId;

    // 🔥 Lý do và mức độ khẩn cấp
    @NotBlank(message = "Lý do truyền máu không được bỏ trống")
    private String reason;

    @NotBlank(message = "Mức độ khẩn cấp là bắt buộc")
    private String urgencyLevel;

    @NotBlank(message = "Mã phân loại RED/YELLOW/GREEN là bắt buộc")
    private String triageLevel;

    // 💉 Số lượng
    @NotNull(message = "Số túi máu là bắt buộc")
    private Integer quantityBag;

    @NotNull(message = "Thể tích máu cần là bắt buộc")
    private Integer quantityMl;

    // ⏰ Thời điểm cần máu
    @NotNull(message = "Ngày cần máu là bắt buộc")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime neededAt;

    // 🧪 Lịch sử truyền máu & phản ứng
    private Boolean crossmatchRequired;
    private Boolean hasTransfusionHistory;
    private Boolean hasReactionHistory;
    private Boolean isPregnant;
    private Boolean hasAntibodyIssue;

    // ⚠️ Ghi chú
    private String warningNote;
    private String specialNote;

    // 💰 Thanh toán
    private Boolean deferredPayment;
    private String deferredPaymentReason;

    // 🩺 Mã bệnh án (tùy chọn)
    private String patientRecordCode;

    // 🆘 Các trường bổ sung cho phân tích nâng cao
    private Boolean isUnmatched;              // Có phải truyền máu không tương thích không?
    private Long codeRedId;                   // Nếu liên quan đến sự kiện Code Red
    private String internalPriorityCode;      // Mã ưu tiên nội bộ (VD: R-CRASH-12)
}
