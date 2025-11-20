package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.chat.ChatMessageRequest;
import org.example.dto.response.chat.ChatMessageResponse;
import org.example.dto.response.chat.ChatRoomResponse;
import org.example.entity.ChatRoom;
import org.example.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Tin nhắn (Chat)", description = "API quản lý chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @GetMapping("/version")
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok("v2-room_type");
    }

    @PostMapping("/rooms")
    @Operation(summary = "Tạo hoặc lấy phòng chat", description = "Tạo phòng chat mới hoặc lấy phòng chat hiện có giữa user và admin")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "404", description = "User hoặc Admin không tồn tại")
    })
    public ResponseEntity<ChatRoomResponse> getOrCreateRoom(
            @Parameter(description = "ID của User", required = true) @RequestParam Long userId,
            @Parameter(description = "ID của Admin", required = true) @RequestParam Long adminId) {
        ChatRoom room = chatService.getOrCreateChatRoom(userId, adminId);
        ChatRoomResponse response = ChatRoomResponse.builder()
                .id(room.getId())
                .type(room.getType().name())
                .status(room.getStatus().name())
                .userId(room.getUser().getId())
                .adminId(room.getAdmin() != null ? room.getAdmin().getId() : null)
                .aiEnabled(room.getAiEnabled())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rooms/{roomId}/messages")
    @Operation(summary = "Lấy lịch sử chat", description = "Lấy danh sách tin nhắn trong phòng chat")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<ChatMessageResponse>> getChatHistory(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String roomId) {
        return ResponseEntity.ok(chatService.getChatHistory(roomId));
    }

    @PostMapping("/rooms/{roomId}/send")
    @Operation(summary = "Gửi tin nhắn", description = "Gửi tin nhắn vào phòng chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Gửi tin nhắn thành công"),
        @ApiResponse(responseCode = "404", description = "Phòng chat hoặc người gửi không tồn tại")
    })
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @Parameter(description = "ID của phòng chat", required = true) @PathVariable String roomId,
            @Parameter(description = "ID của người gửi", required = true) @RequestParam Long senderId,
            @RequestBody ChatMessageRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(roomId, senderId, request));
    }
    
    /**
     * WebSocket handler để gửi tin nhắn
     * Client gửi đến: /app/chat/{roomId}/sendMessage
     * Server broadcast đến: /topic/chat/{roomId}
     */
    @MessageMapping("/chat/{roomId}/sendMessage")
    public ChatMessageResponse sendMessageSocket(
            @DestinationVariable String roomId,
            @Payload ChatMessageRequest request) {
        return chatService.sendMessage(roomId, request.getSenderId(), request);
    }
    
    /**
     * WebSocket handler để thông báo user đang typing
     * Client gửi đến: /app/chat/{roomId}/typing
     * Server broadcast đến: /topic/chat/{roomId}/typing
     */
    @MessageMapping("/chat/{roomId}/typing")
    public Map<String, Object> notifyTyping(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> payload) {
        // payload có thể chứa: userId, userName, isTyping
        return payload;
    }
    
    /**
     * WebSocket handler để mark message as seen
     * Client gửi đến: /app/chat/{roomId}/seen
     * Server broadcast đến: /topic/chat/{roomId}/seen
     */
    @MessageMapping("/chat/{roomId}/seen")
    public Map<String, Object> markAsSeen(
            @DestinationVariable String roomId,
            @Payload Map<String, Object> payload) {
        // payload chứa: userId, lastSeenMessageId
        Long messageId = Long.valueOf(payload.get("lastSeenMessageId").toString());
        chatService.updateMessageStatus(messageId, "seen");
        return payload;
    }
    
    @PutMapping("/messages/{id}")
    @Operation(summary = "Sửa tin nhắn", description = "Cập nhật nội dung tin nhắn")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "Tin nhắn không tồn tại")
    })
    public ResponseEntity<ChatMessageResponse> updateMessage(
            @Parameter(description = "ID của tin nhắn", required = true) @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String newContent = payload.get("content");
        return ResponseEntity.ok(chatService.updateMessage(id, newContent));
    }

    @PutMapping("/messages/{id}/status")
    @Operation(summary = "Cập nhật trạng thái tin nhắn", description = "Cập nhật trạng thái tin nhắn (ví dụ: seen)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "Tin nhắn không tồn tại")
    })
    public ResponseEntity<ChatMessageResponse> updateMessageStatus(
            @Parameter(description = "ID của tin nhắn", required = true) @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        return ResponseEntity.ok(chatService.updateMessageStatus(id, status));
    }
    
    @DeleteMapping("/messages/{id}")
    @Operation(summary = "Xóa tin nhắn", description = "Xóa tin nhắn theo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "Tin nhắn không tồn tại")
    })
    public ResponseEntity<Void> deleteMessage(
            @Parameter(description = "ID của tin nhắn", required = true) @PathVariable Long id) {
        chatService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
