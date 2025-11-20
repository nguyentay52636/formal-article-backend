package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.chat.ChatMessageRequest;
import org.example.dto.response.chat.ChatMessageResponse;
import org.example.entity.ChatMessage;
import org.example.entity.ChatRoom;
import org.example.entity.User;
import org.example.repository.ChatMessageRepository;
import org.example.repository.ChatRoomRepository;
import org.example.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final org.example.mapper.RoomChatMapper roomChatMapper;

    @Transactional
    public ChatRoom getOrCreateChatRoom(Long userId, Long adminId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return chatRoomRepository.findByUserAndAdmin(user, admin)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setId(UUID.randomUUID().toString());
                    room.setUser(user);
                    room.setAdmin(admin);
                    room.setType(ChatRoom.RoomType.user_admin);
                    return chatRoomRepository.save(room);
                });
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatHistory(String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        
        return chatMessageRepository.findByRoomOrderByCreatedAtAsc(room).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageResponse sendMessage(String roomId, Long senderId, ChatMessageRequest request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatMessage message = new ChatMessage();
        message.setRoom(room);
        message.setSender(sender);
        // Determine sender type based on user role or context (simplified here)
        // Assuming senderId matches room.getAdmin().getId() -> admin, else user
        if (room.getAdmin() != null && senderId.equals(room.getAdmin().getId())) {
            message.setSenderType(ChatMessage.SenderType.admin);
        } else {
            message.setSenderType(ChatMessage.SenderType.user);
        }
        
        message.setContent(request.getContent());
        message.setType(request.getType() != null ? request.getType() : ChatMessage.MessageType.text);
        message.setFileUrl(request.getFileUrl());
        message.setFileSize(request.getFileSize());
        message.setFileMime(request.getFileMime());
        
        if (request.getReplyToId() != null) {
            ChatMessage replyTo = chatMessageRepository.findById(request.getReplyToId())
                    .orElse(null);
            message.setReplyTo(replyTo);
        }

        ChatMessage savedMessage = chatMessageRepository.save(message);
        ChatMessageResponse response = mapToResponse(savedMessage);

        // Send to WebSocket topic
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, response);

        return response;
    }
    
    @Transactional
    public ChatMessageResponse updateMessage(Long messageId, String newContent) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        message.setContent(newContent);
        ChatMessage updatedMessage = chatMessageRepository.save(message);
        
        ChatMessageResponse response = mapToResponse(updatedMessage);
        // Notify update via WebSocket (optional, depending on client handling)
        messagingTemplate.convertAndSend("/topic/chat/" + message.getRoom().getId() + "/update", response);
        
        return response;
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        String roomId = message.getRoom().getId();
        chatMessageRepository.delete(message);
        
        // Notify delete via WebSocket
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/delete", messageId);
    }

    private ChatMessageResponse mapToResponse(ChatMessage message) {
        ChatMessageResponse response = new ChatMessageResponse();
        response.setId(message.getId());
        response.setRoomId(message.getRoom().getId());
        if (message.getSender() != null) {
            response.setSenderId(message.getSender().getId());
        }
        response.setSenderType(message.getSenderType());
        response.setContent(message.getContent());
        response.setType(message.getType());
        response.setFileUrl(message.getFileUrl());
        response.setFileSize(message.getFileSize());
        response.setFileMime(message.getFileMime());
        if (message.getReplyTo() != null) {
            response.setReplyToId(message.getReplyTo().getId());
        }
        response.setStatus(message.getStatus());
        response.setCreatedAt(message.getCreatedAt());
        return response;
    }

    // RoomChat CRUD
    /**
     * Tạo phòng chat mới - User tạo yêu cầu chat với Admin
     * Flow: User tạo room → status = pending, adminId = null → gửi notification cho TẤT CẢ admin
     */
    @Transactional
    public ChatRoom createRoom(org.example.dto.request.roomChat.CreateRoomRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo room với status pending và adminId = null (chưa có admin xác nhận)
        ChatRoom room = new ChatRoom();
        room.setId(UUID.randomUUID().toString());
        room.setUser(user);
        room.setAdmin(null); // Admin sẽ được assign khi approve
        
        // Set room type
        if (request.getRoomType() != null) {
            try {
                room.setType(ChatRoom.RoomType.valueOf(request.getRoomType()));
            } catch (IllegalArgumentException e) {
                room.setType(ChatRoom.RoomType.user_admin);
            }
        } else {
            room.setType(ChatRoom.RoomType.user_admin);
        }
        
        room.setAiEnabled(false);
        room.setStatus(ChatRoom.RoomStatus.pending);
        
        ChatRoom savedRoom = chatRoomRepository.save(room);
        
        // Nếu có tin nhắn đầu tiên, lưu luôn vào database
        if (request.getInitialMessage() != null && !request.getInitialMessage().trim().isEmpty()) {
            ChatMessage initialMessage = new ChatMessage();
            initialMessage.setRoom(savedRoom);
            initialMessage.setSender(user);
            initialMessage.setSenderType(ChatMessage.SenderType.user);
            initialMessage.setContent(request.getInitialMessage());
            initialMessage.setType(ChatMessage.MessageType.text);
            initialMessage.setStatus(ChatMessage.MessageStatus.sent);
            chatMessageRepository.save(initialMessage);
        }
        
        // Gửi notification cho TẤT CẢ admin
        notificationService.notifyAllAdmins(
            "Yêu cầu chat mới",
            "User " + user.getFullName() + " muốn chat với bạn. Nhấn để xem và xác nhận.",
            org.example.entity.Notification.NotificationType.chat_message,
            savedRoom
        );
        
        return savedRoom;
    }

    /**
     * Admin xác nhận phòng chat
     * Flow: Admin approve → assign adminId, chuyển status từ pending → active
     * @param roomId ID của phòng chat
     * @param adminId ID của admin đang approve
     */
    @Transactional
    public ChatRoom approveRoom(String roomId, Long adminId) {
        ChatRoom room = getRoomById(roomId);
        
        // Kiểm tra room đang pending
        if (room.getStatus() != ChatRoom.RoomStatus.pending) {
            throw new RuntimeException("Room is not in pending status");
        }
        
        // Lấy thông tin admin
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        // Kiểm tra có phải admin không
        if (!admin.getRole().getName().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("User is not an admin");
        }
        
        // Assign admin và chuyển status sang active
        room.setAdmin(admin);
        room.setStatus(ChatRoom.RoomStatus.active);
        ChatRoom savedRoom = chatRoomRepository.save(room);
        
        // Notify User rằng yêu cầu đã được chấp nhận
        notificationService.createNotificationWithRoom(
            room.getUser().getId(),
            "Yêu cầu chat được chấp nhận",
            "Admin " + admin.getFullName() + " đã chấp nhận yêu cầu chat của bạn.",
            org.example.entity.Notification.NotificationType.chat_message,
            savedRoom
        );
        
        // Broadcast room update via WebSocket
        messagingTemplate.convertAndSend("/topic/chat/room/" + roomId + "/status", roomChatMapper.toResponse(savedRoom));
        
        // Notify user specifically
        messagingTemplate.convertAndSend("/topic/user/" + room.getUser().getId() + "/room-updates", roomChatMapper.toResponse(savedRoom));
        
        return savedRoom;
    }


    @Transactional(readOnly = true)
    public ChatRoom getRoomById(String id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getAllRooms() {
        return chatRoomRepository.findAll();
    }

    @Transactional
    public ChatRoom updateRoom(String id, org.example.dto.request.roomChat.UpdateRoomRequest request) {
        ChatRoom room = getRoomById(id);
        if (request.getAiEnabled() != null) {
            room.setAiEnabled(request.getAiEnabled());
        }
        if (request.getStatus() != null) {
            try {
                room.setStatus(ChatRoom.RoomStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                // Ignore invalid status or throw exception
            }
        }
        return chatRoomRepository.save(room);
    }

    @Transactional
    public ChatMessageResponse updateMessageStatus(Long messageId, String status) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        
        try {
            message.setStatus(ChatMessage.MessageStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
             throw new RuntimeException("Invalid status");
        }
        
        ChatMessage updatedMessage = chatMessageRepository.save(message);
        return mapToResponse(updatedMessage);
    }

    @Transactional
    public void deleteRoom(String id) {
        ChatRoom room = getRoomById(id);
        chatRoomRepository.delete(room);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getRoomsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return chatRoomRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getRoomsByAdminId(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return chatRoomRepository.findByAdmin(admin);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> getRoomsByStatus(String status) {
        try {
            ChatRoom.RoomStatus roomStatus = ChatRoom.RoomStatus.valueOf(status);
            return chatRoomRepository.findByStatus(roomStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }
    
    /**
     * Lấy tất cả pending rooms (dành cho admin để xem các yêu cầu chờ xử lý)
     */
    @Transactional(readOnly = true)
    public List<ChatRoom> getPendingRooms() {
        return chatRoomRepository.findByStatus(ChatRoom.RoomStatus.pending);
    }

    @Transactional
    public ChatRoom closeRoom(String roomId) {
        ChatRoom room = getRoomById(roomId);
        room.setStatus(ChatRoom.RoomStatus.closed);
        ChatRoom savedRoom = chatRoomRepository.save(room);
        
        // Notify via WebSocket
        messagingTemplate.convertAndSend("/topic/chat/" + roomId + "/update", roomChatMapper.toResponse(savedRoom));
        
        return savedRoom;
    }
}
