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
 * Entity cho bảng user
 * Lưu trữ thông tin người dùng
 */
@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"avatar", "fileUploads", "generatedCvs", "favouriteCvs", "comments", "ratings"})
@ToString(exclude = {"avatar", "fileUploads", "generatedCvs", "favouriteCvs", "comments", "ratings"})
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
    
    @Column(name = "password", nullable = false, length = 255)
    @JsonIgnore // Không trả về password trong JSON response
    private String password;
    
    @Column(name = "full_name", length = 150)
    private String fullName;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    @JsonIgnore
    private FileUpload avatar;
    
    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    // Relationships - các file upload của user
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FileUpload> fileUploads = new ArrayList<>();
    
    // Relationships - các CV đã tạo (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GeneratedCv> generatedCvs = new ArrayList<>();
    
    // Relationships - các template yêu thích (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<FavouriteCv> favouriteCvs = new ArrayList<>();
    
    // Relationships - các comment của user (không cascade vì không có ON DELETE CASCADE)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();
    
    // Relationships - các rating của user (không cascade vì không có ON DELETE CASCADE)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
}
