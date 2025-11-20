package org.example.dto.response.notification;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private String type;
    private String roomId;
    private Boolean isRead;
    private LocalDateTime readAt;
    private String metadata;
    private LocalDateTime createdAt;
}
