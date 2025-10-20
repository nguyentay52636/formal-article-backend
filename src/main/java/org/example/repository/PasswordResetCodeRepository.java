package org.example.repository;

import org.example.entity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {
    
    Optional<PasswordResetCode> findByEmailAndResetCodeAndUsedFalseAndExpiresAtAfter(
            String email, String resetCode, LocalDateTime now);
    
    @Modifying
    @Query("UPDATE PasswordResetCode p SET p.used = true WHERE p.email = :email AND p.resetCode = :resetCode")
    void markAsUsed(@Param("email") String email, @Param("resetCode") String resetCode);
    
    @Modifying
    @Query("DELETE FROM PasswordResetCode p WHERE p.expiresAt < :now")
    void deleteExpiredCodes(@Param("now") LocalDateTime now);
    
    @Query("SELECT COUNT(p) FROM PasswordResetCode p WHERE p.email = :email AND p.createdAt > :since AND p.used = false")
    long countRecentUnusedCodes(@Param("email") String email, @Param("since") LocalDateTime since);
}
