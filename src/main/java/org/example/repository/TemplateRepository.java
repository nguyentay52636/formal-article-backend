package org.example.repository;

import org.example.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    // Tìm template theo slug
    Optional<Template> findBySlug(String slug);

    // Kiểm tra slug đã tồn tại chưa
    boolean existsBySlug(String slug);

    // Tìm tất cả template theo tag
    List<Template> findByTagId(Long tagId);

    // Tìm template theo tên chứa keyword (tìm kiếm)
    List<Template> findByNameContainingIgnoreCase(String keyword);

    // Có thể kết hợp tên và tag
    List<Template> findByNameContainingIgnoreCaseAndTagId(String keyword, Long tagId);
}
