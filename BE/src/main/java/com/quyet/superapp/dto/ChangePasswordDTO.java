package com.quyet.superapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {


    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}
