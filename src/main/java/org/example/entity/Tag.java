package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity cho bảng tag
 * Lưu trữ các nhóm/tag cho CV template
 */
@Entity
@Table(name = "tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"templates"})
@ToString(exclude = {"templates"})
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "slug", nullable = false, unique = true, length = 150)
    private String slug;
    
    @Column(name = "name", nullable = false, length = 150)
    private String name;
    
    // Relationships - các template thuộc tag này
    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Template> templates = new ArrayList<>();
}
