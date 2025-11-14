package org.example.repository;

import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm user theo email
    Optional<User> findByEmail(String email);
    
    // Kiểm tra email đã tồn tại chưa
    boolean existsByEmail(String email);
    
    // Tìm user theo phone
    Optional<User> findByPhone(String phone);
    
    // Kiểm tra phone đã tồn tại chưa
    boolean existsByPhone(String phone);
}

