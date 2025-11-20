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

import java.time.LocalDateTime;


@Entity
@Table(name = "chat_message",
       indexes = {
           @Index(name = "idx_room_id", columnList = "room_id"),
           @Index(name = "idx_sender_id", columnList = "sender_id"),
           @Index(name = "idx_created_at", columnList = "created_at")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"room", "sender", "replyTo"})
@ToString(exclude = {"room", "sender", "replyTo"})
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    @JsonIgnore
    private ChatRoom room;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @JsonIgnore
    private User sender;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private SenderType senderType = SenderType.user;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MessageType type = MessageType.text;
    
    @Column(name = "file_url", length = 500)
    private String fileUrl;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "file_mime", length = 150)
    private String fileMime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_to")
    @JsonIgnore
    private ChatMessage replyTo;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status = MessageStatus.sent;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    public enum SenderType {
        user, admin, ai
    }
    
    public enum MessageType {
        text, image, video, audio, file
    }
    
    public enum MessageStatus {
        sent, delivered, seen
    }
}

