package org.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity cho bảng chat_room
 * Lưu trữ các phòng chat (WebSocket Chat Rooms)
 */
@Entity
@Table(name = "chat_room",
       indexes = {
           @Index(name = "idx_user_id", columnList = "user_id"),
           @Index(name = "idx_admin_id", columnList = "admin_id"),
           @Index(name = "idx_status", columnList = "status")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user", "admin", "messages", "notifications"})
@ToString(exclude = {"user", "admin", "messages", "notifications"})
public class ChatRoom {
    
    @Id
    @Column(name = "id", length = 100)
    private String id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType type;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    
    @JsonIgnore
    private User admin;
    
    @Column(name = "ai_enabled", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean aiEnabled = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RoomStatus status = RoomStatus.pending;

    public enum RoomStatus {
        pending, active, closed, timeout
    }
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Relationships - các message trong room này (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ChatMessage> messages = new ArrayList<>();
    
    // Relationships - các notification liên quan đến room này (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();
    
    public enum RoomType {
        user_admin, user_ai
    }
}

