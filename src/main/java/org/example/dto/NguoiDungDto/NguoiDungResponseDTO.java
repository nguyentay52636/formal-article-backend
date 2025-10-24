package org.example.dto.NguoiDungDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.VaiTroDto.VaiTroResponseDTO;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for user information")
public class NguoiDungResponseDTO {

    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "Username", example = "johndoe123")
    private String tenDangNhap;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Full name", example = "John Doe")
    private String hoTen;

    @Schema(description = "Profile picture file ID", example = "1")
    private Long anhDaiDienId;

    @Schema(description = "Gender", example = "Nam")
    private String gioiTinh;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Date of birth", example = "1990-01-15 00:00:00")
    private LocalDateTime ngaySinh;

    @Schema(description = "Address", example = "123 Main St, City")
    private String diaChi;

    @Schema(description = "Phone number", example = "+84123456789")
    private String soDienThoai;

    @Schema(description = "User status", example = "active")
    private String trangThai;

    @Schema(description = "Additional information", example = "Additional user details")
    private String thongTinBoSung;

    @Schema(description = "User role information")
    private VaiTroResponseDTO vaiTro;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Creation timestamp", example = "2024-01-15 10:30:00")
    private LocalDateTime ngayTao;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Last update timestamp", example = "2024-01-20 14:45:00")
    private LocalDateTime ngayCapNhat;
}
