package org.example.repository;

import org.example.entity.BaiVietTepTin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiVietTepTinRepository extends JpaRepository<BaiVietTepTin, Long> {
    
    List<BaiVietTepTin> findByBaiVietId(Long baiVietId);
    
    List<BaiVietTepTin> findByTepTinId(Long tepTinId);
    
    @Query("SELECT bvtt FROM BaiVietTepTin bvtt WHERE bvtt.baiViet.id = :baiVietId AND bvtt.loaiLienKet = :loaiLienKet")
    List<BaiVietTepTin> findByBaiVietIdAndLoaiLienKet(@Param("baiVietId") Long baiVietId, 
                                                        @Param("loaiLienKet") BaiVietTepTin.LoaiLienKet loaiLienKet);
    
    @Query("SELECT COUNT(bvtt) > 0 FROM BaiVietTepTin bvtt WHERE bvtt.baiViet.id = :baiVietId AND bvtt.tepTin.id = :tepTinId")
    boolean existsByBaiVietIdAndTepTinId(@Param("baiVietId") Long baiVietId, @Param("tepTinId") Long tepTinId);
}

