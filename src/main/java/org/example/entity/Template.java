package org.example.entity;

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
 * Entity cho bảng template
 * Lưu trữ các mẫu CV template
 */
@Entity
@Table(name = "template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"tag", "generatedCvs", "favouriteCvs", "comments", "ratings"})
@ToString(exclude = {"tag", "generatedCvs", "favouriteCvs", "comments", "ratings"})
public class Template {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "slug", nullable = false, unique = true, length = 200)
    private String slug;
    
    @Column(name = "summary", length = 300)
    private String summary;
    
    @Column(name = "html", columnDefinition = "LONGTEXT")
    private String html;
    
    @Column(name = "css", columnDefinition = "LONGTEXT")
    private String css;
    
    @Column(name = "preview_url", length = 500)
    private String previewUrl;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "language", length = 100)
    private String language;

    @Column(name = "usage_field", length = 200)
    private String usage;

    @Column(name = "design", length = 150)
    private String design;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "template_features",
            joinColumns = @JoinColumn(name = "template_id")
    )
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;
    
    @Column(name = "views", nullable = false)
    private Long views = 0L;
    
    @Column(name = "downloads", nullable = false)
    private Long downloads = 0L;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships - các CV đã tạo từ template này (cascade ALL để xóa template sẽ xóa luôn CV)
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GeneratedCv> generatedCvs = new ArrayList<>();
    
    // Relationships - các user yêu thích template này (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavouriteCv> favouriteCvs = new ArrayList<>();
    
    // Relationships - các comment của template (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    
    // Relationships - các rating của template (cascade ALL vì ON DELETE CASCADE trong SQL)
    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating> ratings = new ArrayList<>();
}
