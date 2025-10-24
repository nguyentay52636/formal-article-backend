package org.example.repository;

import org.example.entity.BaiViet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BaiVietRepository extends JpaRepository<BaiViet, Long> {
    
    Optional<BaiViet> findByDuongDan(String duongDan);
    
    @Query("SELECT COUNT(b) > 0 FROM BaiViet b WHERE b.duongDan = :duongDan AND b.id != :id")
    boolean existsByDuongDanAndIdNot(@Param("duongDan") String duongDan, @Param("id") Long id);
    
    List<BaiViet> findByDanhMucId(Long danhMucId);
    
    Page<BaiViet> findByDanhMucId(Long danhMucId, Pageable pageable);
    
    List<BaiViet> findByTacGiaId(Long tacGiaId);
    
    Page<BaiViet> findByTacGiaId(Long tacGiaId, Pageable pageable);
    
    Page<BaiViet> findByTrangThai(BaiViet.TrangThai trangThai, Pageable pageable);
    
    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = :trangThai AND b.ngayXuatBan <= :now ORDER BY b.ngayXuatBan DESC")
    List<BaiViet> findPublishedArticles(@Param("now") LocalDateTime now, @Param("trangThai") BaiViet.TrangThai trangThai);
    
    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = :trangThai AND b.ngayXuatBan <= :now ORDER BY b.ngayXuatBan DESC")
    Page<BaiViet> findPublishedArticles(@Param("now") LocalDateTime now, @Param("trangThai") BaiViet.TrangThai trangThai, Pageable pageable);

    @Query("SELECT b FROM BaiViet b WHERE (LOWER(b.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.tomTat) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND b.trangThai = :trangThai")
    List<BaiViet> searchByKeyword(@Param("keyword") String keyword, @Param("trangThai") BaiViet.TrangThai trangThai);

    @Query("SELECT b FROM BaiViet b WHERE (LOWER(b.tieuDe) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(b.tomTat) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND b.trangThai = :trangThai")
    Page<BaiViet> searchByKeyword(@Param("keyword") String keyword, @Param("trangThai") BaiViet.TrangThai trangThai, Pageable pageable);

    @Query("SELECT b FROM BaiViet b JOIN b.thes t WHERE t.id = :theId AND b.trangThai = :trangThai")
    List<BaiViet> findByTheId(@Param("theId") Long theId, @Param("trangThai") BaiViet.TrangThai trangThai);

    @Query("SELECT b FROM BaiViet b JOIN b.thes t WHERE t.id = :theId AND b.trangThai = :trangThai")
    Page<BaiViet> findByTheId(@Param("theId") Long theId, @Param("trangThai") BaiViet.TrangThai trangThai, Pageable pageable);

    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = :trangThai ORDER BY b.ngayXuatBan DESC")
    List<BaiViet> findLatestArticles(@Param("trangThai") BaiViet.TrangThai trangThai);

    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = :trangThai ORDER BY b.ngayXuatBan DESC")
    Page<BaiViet> findLatestArticles(@Param("trangThai") BaiViet.TrangThai trangThai, Pageable pageable);

    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = :trangThai ORDER BY SIZE(b.phanUngs) DESC")
    List<BaiViet> findPopularArticles(@Param("trangThai") BaiViet.TrangThai trangThai);

    @Query("SELECT b FROM BaiViet b WHERE b.trangThai = :trangThai ORDER BY SIZE(b.phanUngs) DESC")
    Page<BaiViet> findPopularArticles(@Param("trangThai") BaiViet.TrangThai trangThai, Pageable pageable);

    @Query("SELECT b FROM BaiViet b WHERE b.ngayXuatBan BETWEEN :startDate AND :endDate AND b.trangThai = :trangThai")
    List<BaiViet> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("trangThai") BaiViet.TrangThai trangThai);

    @Query("SELECT b FROM BaiViet b WHERE b.ngayXuatBan BETWEEN :startDate AND :endDate AND b.trangThai = :trangThai")
    Page<BaiViet> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("trangThai") BaiViet.TrangThai trangThai, Pageable pageable);

    // Get featured articles (join with noi_bat_bai_viet)
    @Query("SELECT DISTINCT b FROM BaiViet b JOIN NoiBatBaiViet nbbv ON b.id = nbbv.baiVietId WHERE b.trangThai = :trangThai ORDER BY nbbv.viTri ASC, b.ngayXuatBan DESC")
    List<BaiViet> findNoiBatBaiViet(@Param("trangThai") BaiViet.TrangThai trangThai);

    long countByTrangThai(BaiViet.TrangThai trangThai);

    long countByTacGiaId(Long tacGiaId);

    long countByDanhMucId(Long danhMucId);
}
