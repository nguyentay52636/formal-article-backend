package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity cho bảng file_upload
 * Lưu trữ thông tin các file được upload (ảnh, document, etc.)
 */
@Entity
@Table(name = "file_upload")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user", "usersWithAvatar", "templates", "generatedCvs"})
@ToString(exclude = {"user", "usersWithAvatar", "templates", "generatedCvs"})
public class FileUpload {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private FileType type;
    
    @Column(name = "mime_type", length = 100)
    private String mimeType;
    
    @Column(name = "file_name", length = 255)
    private String fileName;
    
    @Column(name = "path", length = 500)
    private String path;
    
    @Column(name = "size")
    private Integer size;
    
    @Column(name = "width")
    private Integer width;
    
    @Column(name = "height")
    private Integer height;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Relationships - các User sử dụng file này làm avatar
    @OneToMany(mappedBy = "avatar", fetch = FetchType.LAZY)
    private List<User> usersWithAvatar = new ArrayList<>();
    
    // Relationships - các Template sử dụng file này làm preview image
    @OneToMany(mappedBy = "previewImage", fetch = FetchType.LAZY)
    private List<Template> templates = new ArrayList<>();
    
    // Relationships - các GeneratedCv sử dụng file này làm PDF
    @OneToMany(mappedBy = "pdfFile", fetch = FetchType.LAZY)
    private List<GeneratedCv> generatedCvs = new ArrayList<>();
    
    public enum FileType {
        image, document, other
    }
}
