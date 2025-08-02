package com.quyet.superapp.dto;

import com.quyet.superapp.enums.BloodComponentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BloodComponentDTO {

    private Long bloodComponentId;

    @NotBlank(message = "Tên thành phần không được để trống")
    private String name;

    @NotBlank(message = "Mã viết tắt không được để trống")
    private String code;

    private String storageTemp;

    @Min(value = 1, message = "Số ngày phải lớn hơn 0")
    private Integer storageDays;

    private String usage;

    @NotNull(message = "Vui lòng chọn khả năng apheresis")
    private Boolean isApheresisCompatible;

    @NotNull(message = "Vui lòng chọn loại thành phần")
    private BloodComponentType type;

    /**
     * Đánh dấu active/inactive, dùng để vô hiệu/khôi phục
     */
    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean isActive = true;
}
