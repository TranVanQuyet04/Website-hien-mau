package com.quyet.superapp.mapper;

import com.quyet.superapp.dto.NotificationDTO;
import com.quyet.superapp.entity.Notification;

public class NotificationMapper {
    public static NotificationDTO toDTO(Notification noti) {
        return new NotificationDTO(
                noti.getNotificationId(),
                noti.getUser().getUserId(),
                noti.getContent(),
                noti.getSentAt(),
                noti.getIsRead(),
                noti.getCreatedAt(),    // ✅ mới thêm
                noti.getUpdatedAt()     // ✅ mới thêm
        );
    }
}
