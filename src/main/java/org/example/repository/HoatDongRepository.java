package org.example.repository;

import org.example.entity.LichSuHoatDong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoatDongRepository extends JpaRepository<LichSuHoatDong, Long> {
    
    // Tìm hoạt động theo người thực hiện
    List<LichSuHoatDong> findByNguoiThucHienIdOrderByNgayTaoDesc(Long nguoiThucHienId);
    
    // Tìm hoạt động theo người thực hiện với phân trang
    Page<LichSuHoatDong> findByNguoiThucHienIdOrderByNgayTaoDesc(Long nguoiThucHienId, Pageable pageable);
    
    // Tìm hoạt động theo đối tượng và đối tượng ID
    List<LichSuHoatDong> findByDoiTuongAndDoiTuongIdOrderByNgayTaoDesc(String doiTuong, Long doiTuongId);
    
    // Tìm hoạt động theo hành động
    List<LichSuHoatDong> findByHanhDongOrderByNgayTaoDesc(String hanhDong);
    
    // Tìm hoạt động trong khoảng thời gian
    List<LichSuHoatDong> findByNgayTaoBetweenOrderByNgayTaoDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    // Tìm hoạt động theo người thực hiện và khoảng thời gian
    List<LichSuHoatDong> findByNguoiThucHienIdAndNgayTaoBetweenOrderByNgayTaoDesc(
            Long nguoiThucHienId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Tìm hoạt động theo đối tượng, đối tượng ID và khoảng thời gian
    List<LichSuHoatDong> findByDoiTuongAndDoiTuongIdAndNgayTaoBetweenOrderByNgayTaoDesc(
            String doiTuong, Long doiTuongId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Tìm hoạt động gần đây nhất của một người dùng
    @Query("SELECT h FROM LichSuHoatDong h WHERE h.nguoiThucHienId = :nguoiThucHienId ORDER BY h.ngayTao DESC")
    List<LichSuHoatDong> findRecentActivitiesByUser(@Param("nguoiThucHienId") Long nguoiThucHienId, Pageable pageable);
    
    // Đếm số hoạt động của một người dùng
    long countByNguoiThucHienId(Long nguoiThucHienId);
    
    // Đếm số hoạt động theo đối tượng và đối tượng ID
    long countByDoiTuongAndDoiTuongId(String doiTuong, Long doiTuongId);
    
    // Đếm số hoạt động theo hành động
    long countByHanhDong(String hanhDong);
    
    // Tìm hoạt động theo từ khóa trong mô tả hoặc thông tin bổ sung
    @Query("SELECT h FROM LichSuHoatDong h WHERE " +
           "h.doiTuong LIKE %:keyword% OR " +
           "h.hanhDong LIKE %:keyword% OR " +
           "h.thongTinBoSung LIKE %:keyword% " +
           "ORDER BY h.ngayTao DESC")
    List<LichSuHoatDong> findByKeyword(@Param("keyword") String keyword);
    
    // Tìm hoạt động theo từ khóa với phân trang
    @Query("SELECT h FROM LichSuHoatDong h WHERE " +
           "h.doiTuong LIKE %:keyword% OR " +
           "h.hanhDong LIKE %:keyword% OR " +
           "h.thongTinBoSung LIKE %:keyword% " +
           "ORDER BY h.ngayTao DESC")
    Page<LichSuHoatDong> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Tìm hoạt động theo hành động với phân trang
    Page<LichSuHoatDong> findByHanhDongOrderByNgayTaoDesc(String hanhDong, Pageable pageable);
    
    // Tìm hoạt động theo người thực hiện và hành động với phân trang
    Page<LichSuHoatDong> findByNguoiThucHienIdAndHanhDongOrderByNgayTaoDesc(Long nguoiThucHienId, String hanhDong, Pageable pageable);
    
    // Tìm hoạt động theo người thực hiện và hành động (không phân trang)
    List<LichSuHoatDong> findByNguoiThucHienIdAndHanhDongOrderByNgayTaoDesc(Long nguoiThucHienId, String hanhDong);
    
    // Xóa hoạt động theo người thực hiện
    void deleteByNguoiThucHienId(Long nguoiThucHienId);
}
