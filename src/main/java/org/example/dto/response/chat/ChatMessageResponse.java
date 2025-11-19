package org.example.dto.response.chat;

import lombok.Data;
import org.example.entity.ChatMessage;

import java.time.LocalDateTime;

@Data
public class ChatMessageResponse {
    private Long id;
    private String roomId;
    private Long senderId;
    private ChatMessage.SenderType senderType;
    private String content;
    private ChatMessage.MessageType type;
    private String fileUrl;
    private Long fileSize;
    private String fileMime;
    private Long replyToId;
    private ChatMessage.MessageStatus status;
    private LocalDateTime createdAt;
}
