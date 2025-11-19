package org.example.dto.request.roomChat;

import lombok.Data;

@Data
public class CreateRoomRequest {
    private Long userId;
    private Long adminId;
    private String roomType; // "user_admin" or "user_ai"
}
