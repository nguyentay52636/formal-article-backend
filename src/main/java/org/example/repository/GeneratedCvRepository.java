package org.example.repository;

import org.example.entity.GeneratedCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneratedCvRepository extends JpaRepository<GeneratedCv, Long> {
    
    // Tìm tất cả CV của một user
    List<GeneratedCv> findByUserId(Long userId);
    
    // Tìm tất cả CV của một template
    // Tìm tất cả CV của một template
    List<GeneratedCv> findByTemplateId(Long templateId);

    // Kiểm tra template có đang được sử dụng không
    boolean existsByTemplateId(Long templateId);
}

