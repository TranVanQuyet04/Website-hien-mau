package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonationRegistrationDTO {

    private Long registrationId;  // ✅ phục vụ confirm/get
    private LocalDate scheduledDate;  // ✅ kiểu LocalDate
    private Long slotId;  // ✅ thêm slotId
    private String location;
    private String bloodType;
    private String status;
    // Các thông tin nhập liệu nếu chưa có hồ sơ
    private String fullName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate readyDate;
    private String gender;
    private String phone;
    private Long addressId;
    private String addressFull;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String email;
    private Long bloodTypeId;               // ✅ sửa thành ID
    private String bloodTypeDescription;    // ✅ nếu cần hiển thị tên nhóm máu
}
