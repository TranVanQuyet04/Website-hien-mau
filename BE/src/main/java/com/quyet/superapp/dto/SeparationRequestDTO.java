package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationRequestDTO {
    private Long bloodBagId;
    private SeparationMethod separationMethod;
    private String operator; // ID hoặc tên nhân viên

}
