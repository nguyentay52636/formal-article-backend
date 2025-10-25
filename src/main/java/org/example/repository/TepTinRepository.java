package org.example.repository;

import org.example.entity.TepTin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TepTinRepository extends JpaRepository<TepTin, Long> {
    
    Optional<TepTin> findByDuongDan(String duongDan);
    
    Optional<TepTin> findByTen(String ten);
    
    List<TepTin> findByLoaiTep(String loaiTep);
    
    @Query("SELECT t FROM TepTin t WHERE t.nguoiTao.id = :nguoiTaoId")
    List<TepTin> findByNguoiTaoId(@Param("nguoiTaoId") Long nguoiTaoId);
    
    List<TepTin> findByTrangThai(String trangThai);
    
    boolean existsByDuongDan(String duongDan);
    
    boolean existsByTen(String ten);
    
    @Query("SELECT COUNT(t) > 0 FROM TepTin t WHERE t.duongDan = :duongDan AND t.id != :id")
    boolean existsByDuongDanAndIdNot(@Param("duongDan") String duongDan, @Param("id") Long id);
    
    @Query("SELECT COUNT(t) > 0 FROM TepTin t WHERE t.ten = :ten AND t.id != :id")
    boolean existsByTenAndIdNot(@Param("ten") String ten, @Param("id") Long id);
    
    @Query("SELECT t FROM TepTin t WHERE " +
           "LOWER(t.ten) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.tenTapTin) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TepTin> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT t FROM TepTin t WHERE " +
           "(LOWER(t.ten) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.moTa) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.tenTapTin) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "t.loaiTep = :loaiTep")
    List<TepTin> searchByKeywordAndLoaiTep(@Param("keyword") String keyword, @Param("loaiTep") String loaiTep);
    
    @Query("SELECT t FROM TepTin t ORDER BY t.ngayTao DESC")
    List<TepTin> findAllOrderByNgayTaoDesc();
    
    @Query("SELECT t FROM TepTin t ORDER BY t.ngayTao DESC")
    Page<TepTin> findAllOrderByNgayTaoDesc(Pageable pageable);
    
    // Find by loaiTep with pagination
    Page<TepTin> findByLoaiTep(String loaiTep, Pageable pageable);
    
    // Find by nguoiTaoId with pagination
    @Query("SELECT t FROM TepTin t WHERE t.nguoiTao.id = :nguoiTaoId")
    Page<TepTin> findByNguoiTaoId(@Param("nguoiTaoId") Long nguoiTaoId, Pageable pageable);
    
    // Find by trangThai with pagination
    Page<TepTin> findByTrangThai(String trangThai, Pageable pageable);
    
    // Find files by user and type
    @Query("SELECT t FROM TepTin t WHERE t.nguoiTao.id = :nguoiTaoId AND t.loaiTep = :loaiTep")
    List<TepTin> findByNguoiTaoIdAndLoaiTep(@Param("nguoiTaoId") Long nguoiTaoId, @Param("loaiTep") String loaiTep);
    
    // Count files by type
    @Query("SELECT COUNT(t) FROM TepTin t WHERE t.loaiTep = :loaiTep")
    Long countByLoaiTep(@Param("loaiTep") String loaiTep);
    
    // Count files by user
    @Query("SELECT COUNT(t) FROM TepTin t WHERE t.nguoiTao.id = :nguoiTaoId")
    Long countByNguoiTaoId(@Param("nguoiTaoId") Long nguoiTaoId);
    
    // Get file statistics
    @Query("SELECT t.loaiTep, COUNT(t) FROM TepTin t GROUP BY t.loaiTep")
    List<Object[]> getFileStatisticsByType();
}
