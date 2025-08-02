package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffListDTO {
    private Long userId;
    private String fullName;
    private String staffPosition;
    private boolean enable;
    private String startDate; // nếu cần
    private String email;
    private String phone;
}
