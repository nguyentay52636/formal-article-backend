package org.example.dto.response.roomChat;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class RoomChatResponse {
    private String id;
    private String roomType;
    private Long userId;
    private Long adminId;
    private Boolean aiEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
