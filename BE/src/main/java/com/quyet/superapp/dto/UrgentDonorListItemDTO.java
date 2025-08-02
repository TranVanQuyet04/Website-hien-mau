// Giả sử bạn cần hiển thị danh sách hiến máu khẩn cấp bằng DTO
// Bước 1: Tạo DTO để truyền dữ liệu về FE (dùng cho bảng danh sách)

package com.quyet.superapp.dto;

import com.quyet.superapp.enums.DonorReadinessLevel;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UrgentDonorListItemDTO {
    private Long donorId;
    private String fullName;
    private String phone;
    private String bloodType;
    private DonorReadinessLevel readinessLevel;
    private String readinessDescription;
    private String lastDonationDate;
    private String recoveryTime;
    private String note;
}