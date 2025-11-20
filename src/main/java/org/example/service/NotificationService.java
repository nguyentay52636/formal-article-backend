package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.notification.NotificationResponse;
import org.example.entity.ChatRoom;
import org.example.entity.Notification;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.NotificationMapper;
import org.example.repository.NotificationRepository;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NotificationMapper notificationMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUserNotifications(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(user).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        return notificationMapper.toResponse(notificationRepository.save(notification));
    }
    
    @Transactional
    public void createNotification(Long receiverId, String title, String message, Notification.NotificationType type) {
         User user = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));
         
         Notification notification = new Notification();
         notification.setReceiver(user);
         notification.setTitle(title);
         notification.setMessage(message);
         notification.setType(type);
         notification.setIsRead(false);
         
         Notification savedNotification = notificationRepository.save(notification);
         
         // Send to WebSocket
         if (user.getRole().getName().equalsIgnoreCase("ADMIN")) {
             messagingTemplate.convertAndSend("/topic/admin/notifications", notificationMapper.toResponse(savedNotification));
         } else {
             messagingTemplate.convertAndSend("/topic/user/" + receiverId + "/notifications", notificationMapper.toResponse(savedNotification));
         }
    }
    
    /**
     * Tạo notification với room_id để link đến phòng chat cụ thể
     */
    @Transactional
    public void createNotificationWithRoom(Long receiverId, String title, String message, Notification.NotificationType type, ChatRoom room) {
         User user = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));
         
         Notification notification = new Notification();
         notification.setReceiver(user);
         notification.setTitle(title);
         notification.setMessage(message);
         notification.setType(type);
         notification.setIsRead(false);
         notification.setRoom(room);
         
         Notification savedNotification = notificationRepository.save(notification);
         
         // Send to WebSocket
         if (user.getRole().getName().equalsIgnoreCase("ADMIN")) {
             messagingTemplate.convertAndSend("/topic/admin/notifications", notificationMapper.toResponse(savedNotification));
         } else {
             messagingTemplate.convertAndSend("/topic/user/" + receiverId + "/notifications", notificationMapper.toResponse(savedNotification));
         }
    }
    
    /**
     * Gửi notification cho TẤT CẢ admin (dùng khi user tạo chat room request)
     */
    @Transactional
    public void notifyAllAdmins(String title, String message, Notification.NotificationType type, ChatRoom room) {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Admin role not found"));
        
        List<User> admins = userRepository.findByRoleAndActiveTrue(adminRole);
        
        for (User admin : admins) {
            Notification notification = new Notification();
            notification.setReceiver(admin);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setType(type);
            notification.setIsRead(false);
            notification.setRoom(room);
            
            Notification savedNotification = notificationRepository.save(notification);
            
            // Send to each admin via WebSocket
            messagingTemplate.convertAndSend("/topic/admin/notifications", notificationMapper.toResponse(savedNotification));
        }
    }
}
