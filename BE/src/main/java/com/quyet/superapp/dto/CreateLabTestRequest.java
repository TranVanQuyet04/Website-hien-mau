package com.quyet.superapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLabTestRequest {
    private Long bloodUnitId;    // ID của đơn vị máu cần xét nghiệm
    private Long testedById;     // ID của nhân viên thực hiện xét nghiệm

    // Các kết quả từng bệnh
    private boolean hivNegative;
    private boolean hbvNegative;
    private boolean hcvNegative;
    private boolean syphilisNegative;
    private boolean malariaNegative;

    // ✅ passed sẽ được tính tự động ở backend (tất cả âm tính)
}
