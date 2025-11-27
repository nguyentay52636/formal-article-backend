package org.example.repository;

import org.example.entity.GeneratedCv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneratedCvRepository extends JpaRepository<GeneratedCv, Long> {
    
    /**
     * Tìm CV theo ID với eager fetch user, template và template.features, template.tag
     */
    @EntityGraph(attributePaths = {"user", "template", "template.features", "template.tag"})
    Optional<GeneratedCv> findById(Long id);
    
    /**
     * Lấy tất cả CVs với eager fetch
     */
    @EntityGraph(attributePaths = {"user", "template", "template.features", "template.tag"})
    @Query("SELECT gc FROM GeneratedCv gc ORDER BY gc.createdAt DESC")
    List<GeneratedCv> findAllWithUserAndTemplate();
    
    /**
     * Tìm tất cả CV của một user (không phân trang)
     */
    List<GeneratedCv> findByUserId(Long userId);
    
    /**
     * Tìm tất cả CV của một user (có phân trang)
     */
    Page<GeneratedCv> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Tìm tất cả CV của một user, sắp xếp theo thời gian tạo mới nhất (có phân trang)
     */
    Page<GeneratedCv> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    /**
     * Tìm tất cả CV của một user, sắp xếp theo thời gian tạo mới nhất (không phân trang) - với eager fetch
     */
    @EntityGraph(attributePaths = {"user", "template", "template.features", "template.tag"})
    List<GeneratedCv> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Tìm tất cả CV của một template
     */
    List<GeneratedCv> findByTemplateId(Long templateId);
    
    /**
     * Kiểm tra template có đang được sử dụng không
     */
    boolean existsByTemplateId(Long templateId);
    
    /**
     * Kiểm tra CV có thuộc về user không
     */
    boolean existsByIdAndUserId(Long id, Long userId);
    
    /**
     * Đếm số CV của một user
     */
    long countByUserId(Long userId);
    
    /**
     * Tìm kiếm CV theo title (có phân trang)
     */
    @Query("SELECT gc FROM GeneratedCv gc WHERE gc.user.id = :userId AND LOWER(gc.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<GeneratedCv> searchByUserIdAndTitle(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}
