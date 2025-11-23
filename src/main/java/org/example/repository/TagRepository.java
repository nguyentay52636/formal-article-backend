package org.example.repository;

import org.example.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    // Tìm tag theo slug
    Optional<Tag> findBySlug(String slug);
    
    // Kiểm tra slug đã tồn tại chưa
    // Kiểm tra slug đã tồn tại chưa
    boolean existsBySlug(String slug);

    // Tìm tag theo type
    List<Tag> findByType(String type);
}

