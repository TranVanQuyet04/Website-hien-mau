package com.quyet.superapp.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnverifiedDonorDTO {
    private Long donorRegistryId;
    private Long userId;
    private String fullName;
    private String phone;
    private String bloodType;
    private String gender;

    @JsonFormat(pattern = "dd-MM-yyyy") // ✅ định dạng ngày sinh
    private LocalDate dob;

    private String location;
    private String addressFull;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") // ✅ định dạng thời gian đăng ký
    private LocalDateTime registeredAt;

    private String status;
}


