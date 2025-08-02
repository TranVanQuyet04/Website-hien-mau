package com.quyet.superapp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class BloodMasterDataResponseDTO {
    private List<BloodTypeFullDTO> bloodTypes;       // ← đổi sang FullDTO để có Rh, mô tả, trạng thái
    private List<BloodComponentFullDTO> bloodComponents; // ← đổi sang FullDTO để có nhiệt độ, ứng dụng...
}


