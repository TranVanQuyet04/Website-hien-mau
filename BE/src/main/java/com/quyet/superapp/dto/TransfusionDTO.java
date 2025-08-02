package com.quyet.superapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransfusionDTO {
    private Long id;

    private Long recipientId;       // Người nhận
    private Long requestId;         // Mã yêu cầu
    private Long bloodUnitId;       // Liên kết túi máu

    private String bloodType;       // Nhóm máu (ví dụ: O+)
    private String bloodBagCode;    // Mã túi máu (ví dụ: A1234567)
    private Integer bagCount;       // Số lượng túi máu
    private Integer volumeMl;       // Tổng thể tích máu truyền

    private String triageLevel;     // RED, YELLOW, BLACK
    private String urgencyLevel;    // KHẨN CẤP, BÌNH THƯỜNG...

    private String status;          // COMPLETED, REJECTED, NOT_TRANSFUSED...
    private LocalDateTime transfusionDate; // Thời gian truyền
    private String notes;           // Ghi chú lý do truyền máu

    private String recipientName;   // Tên người nhận
    private String recipientPhone;  // SĐT người nhận

    private String approvedBy;      // Người phê duyệt (admin)


}
