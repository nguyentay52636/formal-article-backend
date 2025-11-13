package org.example.repository;

import org.example.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // Tìm tất cả comment của một template
    List<Comment> findByTemplateId(Long templateId);
    
    // Tìm tất cả comment của một user
    List<Comment> findByUserId(Long userId);
}

