package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.notification.NotificationResponse;
import org.example.entity.Notification;
import org.example.entity.User;
import org.example.mapper.NotificationMapper;
import org.example.repository.NotificationRepository;
import org.example.repository.UserRepository;
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
    private final NotificationMapper notificationMapper;

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
         
         notificationRepository.save(notification);
    }
}
