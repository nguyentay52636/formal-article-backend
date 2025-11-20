package org.example.mapper;

import org.example.dto.response.roomChat.RoomChatResponse;
import org.example.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class RoomChatMapper {

    public RoomChatResponse toResponse(ChatRoom room) {
        if (room == null) {
            return null;
        }
        return RoomChatResponse.builder()
                .id(room.getId())
                .roomType(room.getType().name())
                .status(room.getStatus().name())
                .userId(room.getUser().getId())
                .adminId(room.getAdmin() != null ? room.getAdmin().getId() : null)
                .aiEnabled(room.getAiEnabled())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }
}
