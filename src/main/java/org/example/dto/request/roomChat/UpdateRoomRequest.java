package org.example.dto.request.roomChat;

import lombok.Data;

@Data
public class UpdateRoomRequest {
    private Boolean aiEnabled;
    private String status; // "pending", "active", "closed", "timeout"
}
