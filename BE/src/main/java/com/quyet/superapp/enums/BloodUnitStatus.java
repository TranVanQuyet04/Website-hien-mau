package com.quyet.superapp.enums;

public enum BloodUnitStatus {
    AVAILABLE,      // ✅ Sẵn sàng sử dụng
    RESERVED,       // 🟡 Đã được đặt trước cho một bệnh nhân
    PENDING_TESTING,     // 🧪 Đang xét nghiệm (chưa rõ an toàn)
    QUARANTINED,    // 🚫 Tạm giữ do nghi ngờ vấn đề (ví dụ: test dương tính, nhãn không hợp lệ)
    USED,           // ✅ Đã được truyền cho bệnh nhân
    DISCARDED,      // ❌ Bị loại bỏ (hết hạn, nhiễm khuẩn, lỗi tách...)
    EXPIRED,      // ⏰ Hết hạn sử dụng theo ngày
    FAILED_TEST,// Xét nghiệm không đạt
    STORED
}
