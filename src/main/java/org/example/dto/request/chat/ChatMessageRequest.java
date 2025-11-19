package org.example.dto.request.chat;

import lombok.Data;
import org.example.entity.ChatMessage;

@Data
public class ChatMessageRequest {
    private String content;
    private ChatMessage.MessageType type;
    private String fileUrl;
    private Long fileSize;
    private String fileMime;
    private Long senderId;
    private Long replyToId;
}
