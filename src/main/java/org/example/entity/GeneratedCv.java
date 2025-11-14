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

/**
 * Entity cho bảng generated_cv
 * Lưu trữ các CV đã được tạo từ template
 */
@Entity
@Table(name = "generated_cv")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user", "template"})
@ToString(exclude = {"user", "template"})
public class GeneratedCv {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;
    
    @Column(name = "data_json", nullable = false, columnDefinition = "LONGTEXT")
    private String dataJson;
    
    @Column(name = "style_json", nullable = false, columnDefinition = "LONGTEXT")
    private String styleJson;
    
    @Column(name = "html_output", columnDefinition = "LONGTEXT")
    private String htmlOutput;
    
    @Column(name = "title", length = 200)
    private String title;
    
    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
