package org.example.repository;

import org.example.entity.KhoiNoiBat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhoiNoiBatRepository extends JpaRepository<KhoiNoiBat, Long> {
    
    // Find by ma (exact match)
    Optional<KhoiNoiBat> findByMa(String ma);
    
    // Find by ten (exact match)
    Optional<KhoiNoiBat> findByTen(String ten);
    
    // Find by kich hoat status
    List<KhoiNoiBat> findByKichHoat(Boolean kichHoat);
    
    // Check if ma exists
    boolean existsByMa(String ma);
    
    // Check if ten exists
    boolean existsByTen(String ten);
    
    
    @Query("SELECT COUNT(k) > 0 FROM KhoiNoiBat k WHERE k.ten = :ten AND k.id != :id")
    boolean existsByTenAndIdNot(@Param("ten") String ten, @Param("id") Long id);
    
    @Query("SELECT k FROM KhoiNoiBat k WHERE " +
           "LOWER(k.ma) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(k.ten) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(k.cauHinh) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<KhoiNoiBat> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT k FROM KhoiNoiBat k ORDER BY k.ngayTao DESC")
    List<KhoiNoiBat> findAllOrderByNgayTaoDesc();
    
    @Query("SELECT k FROM KhoiNoiBat k WHERE k.kichHoat = true ORDER BY k.ngayTao DESC")
    List<KhoiNoiBat> findActiveOrderByNgayTaoDesc();
}
