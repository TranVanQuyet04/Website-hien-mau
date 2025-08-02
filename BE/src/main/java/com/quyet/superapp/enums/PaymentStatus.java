package com.quyet.superapp.enums;

public enum PaymentStatus {
    PENDING,    // 🔁 Đã tạo giao dịch nhưng chưa hoàn tất
    SUCCESS,    // ✅ Thanh toán thành công
    FAILED,     // ❌ Giao dịch thất bại (timeout, hủy, lỗi)
    REFUNDED,   // 💸 Đã hoàn tiền
    WAIVED,      // 🆓 Miễn phí (từ thiện, ca đặc biệt)
    DEFERRED  // ➕ Thêm dòng này nếu muốn dùng trạng thái hoãn thanh toán
}

