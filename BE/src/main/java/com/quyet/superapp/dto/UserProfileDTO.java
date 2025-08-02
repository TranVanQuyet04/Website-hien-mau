package com.quyet.superapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileDTO {

    // 🧍 Thông tin cơ bản
    private Long userId;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;
    private String gender;
    private String occupation;

    @NotBlank(message = "CCCD không được để trống")
    @Pattern(regexp = "\\d{12}", message = "CCCD phải gồm đúng 12 chữ số")
    private String citizenId;

    // 📞 Thông tin liên hệ
    private String phone;
    private String landline;
    private String email;

    // 📍 Địa chỉ
    private Long addressId;
    private String addressFull;
    private AddressDTO address;
    private Double latitude;
    private Double longitude;

    // 🩸 Thông tin nhóm máu & sức khỏe
    private Long bloodTypeId;
    private Double weight;
    private Double height;
    private String location;
    private LocalDateTime lastDonationDate;
    private Integer recoveryTime;

    // 💳 Bảo hiểm y tế
    private Boolean hasInsurance;
    private String insuranceCardNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceValidTo;

    // 👔 Quản lý nhân sự
    private String staffPosition;
    private String note;

    // 🕒 Hệ thống
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
