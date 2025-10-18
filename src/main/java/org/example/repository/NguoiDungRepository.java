package org.example.repository;

import org.example.entity.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {

    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    Optional<NguoiDung> findByEmail(String email);

    boolean existsByTenDangNhap(String tenDangNhap);

    boolean existsByEmail(String email);

    List<NguoiDung> findByTrangThai(String trangThai);

    @Query("SELECT n FROM NguoiDung n WHERE n.hoTen LIKE %:keyword% OR n.email LIKE %:keyword% OR n.tenDangNhap LIKE %:keyword%")
    List<NguoiDung> searchByKeyword(@Param("keyword") String keyword);
}
