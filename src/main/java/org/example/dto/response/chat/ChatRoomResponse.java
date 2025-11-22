package org.example.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomResponse {
    private String id;
    private String type;
    private String status;
    private Long userId;
    private Long adminId;
    private Boolean aiEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
