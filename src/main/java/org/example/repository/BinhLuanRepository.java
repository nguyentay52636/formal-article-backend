package org.example.repository;

import org.example.entity.BinhLuan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BinhLuanRepository extends JpaRepository<BinhLuan, Long> {

    List<BinhLuan> findByBaiVietId(Long baiVietId);

    List<BinhLuan> findByNguoiDungId(Long nguoiDungId);

    List<BinhLuan> findByTrangThai(String trangThai);

    List<BinhLuan> findByBaiVietIdAndTrangThai(Long baiVietId, String trangThai);

    @Query("SELECT b FROM BinhLuan b WHERE b.binhLuanCha IS NULL AND b.baiViet.id = :baiVietId AND b.trangThai = :trangThai ORDER BY b.ngayTao ASC")
    List<BinhLuan> findParentCommentsByBaiVietIdAndTrangThai(@Param("baiVietId") Long baiVietId, @Param("trangThai") String trangThai);

    @Query("SELECT b FROM BinhLuan b WHERE b.binhLuanCha.id = :binhLuanChaId AND b.trangThai = :trangThai ORDER BY b.ngayTao ASC")
    List<BinhLuan> findChildCommentsByParentIdAndTrangThai(@Param("binhLuanChaId") Long binhLuanChaId, @Param("trangThai") String trangThai);

    @Query("SELECT b FROM BinhLuan b WHERE LOWER(b.noiDung) LIKE LOWER(CONCAT('%', :keyword, '%')) AND b.trangThai = :trangThai")
    List<BinhLuan> searchByContent(@Param("keyword") String keyword, @Param("trangThai") String trangThai);

    long countByBaiVietId(Long baiVietId);

    long countByBaiVietIdAndTrangThai(Long baiVietId, String trangThai);

    long countByNguoiDungId(Long nguoiDungId);

    @Query("SELECT b FROM BinhLuan b WHERE b.trangThai = :trangThai ORDER BY b.ngayTao DESC")
    List<BinhLuan> findRecentComments(@Param("trangThai") String trangThai);

    List<BinhLuan> findByEmailKhach(String emailKhach);

    @Query("SELECT b FROM BinhLuan b WHERE LOWER(b.tenKhach) LIKE LOWER(CONCAT('%', :tenKhach, '%')) AND b.trangThai = :trangThai")
    List<BinhLuan> findByTenKhachContaining(@Param("tenKhach") String tenKhach, @Param("trangThai") String trangThai);
}
