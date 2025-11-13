package org.example.repository;

import org.example.entity.FavouriteCv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteCvRepository extends JpaRepository<FavouriteCv, Long> {
    
    // Tìm favourite theo user và template
    Optional<FavouriteCv> findByUserIdAndTemplateId(Long userId, Long templateId);
    
    // Kiểm tra user đã yêu thích template chưa
    boolean existsByUserIdAndTemplateId(Long userId, Long templateId);
    
    // Tìm tất cả template yêu thích của user
    List<FavouriteCv> findByUserId(Long userId);
    
    // Tìm tất cả user yêu thích template
    List<FavouriteCv> findByTemplateId(Long templateId);
}

