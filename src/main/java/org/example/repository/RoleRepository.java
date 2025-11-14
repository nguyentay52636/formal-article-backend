package org.example.repository;

import org.example.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // Tìm role theo name
    Optional<Role> findByName(String name);
    
    // Kiểm tra name đã tồn tại chưa
    boolean existsByName(String name);
}

