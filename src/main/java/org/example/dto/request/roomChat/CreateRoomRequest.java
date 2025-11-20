package org.example.dto.request.roomChat;

import lombok.Data;

@Data
public class CreateRoomRequest {
    private Long userId;
    private String roomType; // "user_admin" or "user_ai" - default to "user_admin"
    private String initialMessage; // Tin nhắn đầu tiên khi tạo room (optional)
}
