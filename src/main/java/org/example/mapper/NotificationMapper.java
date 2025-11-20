package org.example.mapper;

import org.example.dto.response.notification.NotificationResponse;
import org.example.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        if (notification == null) {
            return null;
        }
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType().name())
                .roomId(notification.getRoom() != null ? notification.getRoom().getId() : null)
                .isRead(notification.getIsRead())
                .readAt(notification.getReadAt())
                .metadata(notification.getMetadata())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
