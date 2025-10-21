package org.example.mapping;

import org.example.dto.BaiVietDto.BaiVietCreateDto;
import org.example.dto.BaiVietDto.BaiVietResponseDto;
import org.example.dto.BaiVietDto.BaiVietUpdateDto;
import org.example.entity.BaiViet;
import org.example.entity.DanhMuc;
import org.example.entity.NguoiDung;
import org.example.entity.TepTin;
import org.example.entity.The;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BaiVietMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "danhMuc", source = "danhMucId")
    @Mapping(target = "tacGia", ignore = true) 
    @Mapping(target = "anhDaiDien", source = "anhDaiDienId")
    @Mapping(target = "thes", ignore = true) 
    @Mapping(target = "tepTins", ignore = true) 
    @Mapping(target = "binhLuans", ignore = true)
    @Mapping(target = "phanUngs", ignore = true)
    @Mapping(target = "taiLieuDaLuus", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    BaiViet toEntity(BaiVietCreateDto dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "danhMuc", source = "danhMucId")
    @Mapping(target = "tacGia", ignore = true)
    @Mapping(target = "anhDaiDien", source = "anhDaiDienId")
    @Mapping(target = "thes", ignore = true)
    @Mapping(target = "tepTins", ignore = true)
    @Mapping(target = "binhLuans", ignore = true)
    @Mapping(target = "phanUngs", ignore = true)
    @Mapping(target = "taiLieuDaLuus", ignore = true)
    @Mapping(target = "ngayTao", ignore = true)
    @Mapping(target = "ngayCapNhat", ignore = true)
    void updateEntity(BaiVietUpdateDto dto, @MappingTarget BaiViet entity);
    
    @Mapping(target = "danhMucId", source = "danhMuc.id")
    @Mapping(target = "danhMucTen", source = "danhMuc.ten")
    @Mapping(target = "tacGiaId", source = "tacGia.id")
    @Mapping(target = "tacGiaTen", source = "tacGia.hoTen")
    @Mapping(target = "anhDaiDienId", source = "anhDaiDien.id")
    @Mapping(target = "anhDaiDienDuongDan", source = "anhDaiDien.duongDan")
    @Mapping(target = "soLuongBinhLuan", expression = "java(entity.getBinhLuans() != null ? (long) entity.getBinhLuans().size() : 0L)")
    @Mapping(target = "soLuongPhanUng", expression = "java(entity.getPhanUngs() != null ? (long) entity.getPhanUngs().size() : 0L)")
    @Mapping(target = "soLuongLuotXem", ignore = true)
    @Mapping(target = "thes", source = "thes")
    @Mapping(target = "tepTins", source = "tepTins")
    BaiVietResponseDto toResponseDto(BaiViet entity);
    
    List<BaiVietResponseDto> toResponseDtoList(List<BaiViet> entities);
    
    // Helper methods for mapping nested objects
    default BaiVietResponseDto.TheDto mapTheToDto(The the) {
        if (the == null) return null;
        
        BaiVietResponseDto.TheDto dto = new BaiVietResponseDto.TheDto();
        dto.setId(the.getId());
        dto.setTen(the.getTen());
        dto.setDuongDan(the.getDuongDan());
        dto.setMauSac(the.getMauSac());
        return dto;
    }
    
    default BaiVietResponseDto.TepTinDto mapTepTinToDto(TepTin tepTin) {
        if (tepTin == null) return null;
        
        BaiVietResponseDto.TepTinDto dto = new BaiVietResponseDto.TepTinDto();
        dto.setId(tepTin.getId());
        dto.setTen(tepTin.getTen());
        dto.setDuongDan(tepTin.getDuongDan());
        dto.setDinhDang(tepTin.getDinhDang());
        dto.setKichThuoc(tepTin.getKichThuoc());
        dto.setMoTa(tepTin.getMoTa());
        return dto;
    }
    
    default DanhMuc mapDanhMucId(Long danhMucId) {
        if (danhMucId == null) return null;
        
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setId(danhMucId);
        return danhMuc;
    }

    default TepTin mapTepTinId(Long tepTinId) {
        if (tepTinId == null) return null;
        
        TepTin tepTin = new TepTin();
        tepTin.setId(tepTinId);
        return tepTin;
    }
}