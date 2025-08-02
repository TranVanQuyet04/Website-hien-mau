package com.quyet.superapp.dto;

import com.quyet.superapp.enums.SeparationMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeparationOrderDTO {
    private Long separationOrderId;
    private Long bloodBagId; // Túi máu được tách
    private Long performedById; // Nhân viên thực hiện
    private Long apheresisMachineId; // 👈 MÁY ĐÃ DÙNG ĐỂ TÁCH
    private LocalDateTime performedAt;
    private SeparationMethod separationType;
    private String note;
}
