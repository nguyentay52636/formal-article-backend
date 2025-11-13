package org.example.repository;

import org.example.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    // Tìm rating theo template và user
    Optional<Rating> findByTemplateIdAndUserId(Long templateId, Long userId);
    
    // Kiểm tra user đã đánh giá template chưa
    boolean existsByTemplateIdAndUserId(Long templateId, Long userId);
    
    // Tìm tất cả rating của một template
    List<Rating> findByTemplateId(Long templateId);
    
    // Tìm tất cả rating của một user
    List<Rating> findByUserId(Long userId);
    
    // Tính điểm trung bình của template
    // Sử dụng @Query nếu cần tính toán phức tạp hơn
}

