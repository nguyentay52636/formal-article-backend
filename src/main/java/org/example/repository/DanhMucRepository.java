package org.example.repository;

import org.example.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {
    
    // Find by duong dan
    Optional<DanhMuc> findByDuongDan(String duongDan);
    
    // Find by ten (exact match)
    Optional<DanhMuc> findByTen(String ten);
    
    // Find by kich hoat status
    List<DanhMuc> findByKichHoat(Boolean kichHoat);
    
    // Check if duong dan exists
    boolean existsByDuongDan(String duongDan);
    
    // Check if ten exists
    boolean existsByTen(String ten);
    
    // Check if duong dan exists (excluding current record for update)
    @Query("SELECT COUNT(d) > 0 FROM DanhMuc d WHERE d.duongDan = :duongDan AND d.id != :id")
    boolean existsByDuongDanAndIdNot(@Param("duongDan") String duongDan, @Param("id") Long id);
    
    // Check if ten exists (excluding current record for update)
    @Query("SELECT COUNT(d) > 0 FROM DanhMuc d WHERE d.ten = :ten AND d.id != :id")
    boolean existsByTenAndIdNot(@Param("ten") String ten, @Param("id") Long id);
    
    // Search by keyword in ten or moTa
    @Query("SELECT d FROM DanhMuc d WHERE " +
           "LOWER(d.ten) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(d.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<DanhMuc> searchByKeyword(@Param("keyword") String keyword);
    
    // Find all ordered by thu tu
    @Query("SELECT d FROM DanhMuc d ORDER BY d.thuTu ASC")
    List<DanhMuc> findAllOrderByThuTu();
    
    // Find all ordered by creation date
    @Query("SELECT d FROM DanhMuc d ORDER BY d.ngayTao DESC")
    List<DanhMuc> findAllOrderByNgayTaoDesc();
    
    // Find all active categories ordered by thu tu
    @Query("SELECT d FROM DanhMuc d WHERE d.kichHoat = true ORDER BY d.thuTu ASC")
    List<DanhMuc> findActiveOrderByThuTu();
}
