package com.quyet.superapp.enums;

public enum InventoryCheckResultStatus {
    AVAILABLE,             // Đủ máu đúng yêu cầu
    PARTIAL_AVAILABLE,     // Có thể thay thế nhóm tương thích
    INSUFFICIENT,          // Không đủ máu
    LOW_AFTER_DISPATCH,    // Đủ, nhưng kho sẽ bị cảnh báo
    UNKNOWN_COMPONENT_TYPE // Không nhận diện được thành phần hoặc nhóm máu
}

