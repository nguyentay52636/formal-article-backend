package org.example.dto.BinhLuanDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for comment information")
public class BinhLuanResponseDto {

    @Schema(description = "Comment ID", example = "1")
    private Long id;

    @Schema(description = "Article ID", example = "1")
    private Long baiVietId;

    @Schema(description = "User ID", example = "1")
    private Long nguoiDungId;

    @Schema(description = "User information")
    private UserInfoDto nguoiDung;

    @Schema(description = "Comment content", example = "This is a great article!")
    private String noiDung;

    @Schema(description = "Guest name", example = "John Doe")
    private String tenKhach;

    @Schema(description = "Guest email", example = "john.doe@example.com")
    private String emailKhach;

    @Schema(description = "Parent comment ID", example = "1")
    private Long binhLuanChaId;

    @Schema(description = "Child comments count", example = "3")
    private Integer soLuongBinhLuanCon;

    @Schema(description = "Comment status", example = "active")
    private String trangThai;

    @Schema(description = "Additional information", example = "Additional comment details")
    private String thongTinBoSung;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Creation timestamp", example = "2024-01-15T10:30:00")
    private LocalDateTime ngayTao;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Last update timestamp", example = "2024-01-20T14:45:00")
    private LocalDateTime ngayCapNhat;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "User information for comment")
    public static class UserInfoDto {
        @Schema(description = "User ID", example = "1")
        private Long id;

        @Schema(description = "Username", example = "johndoe123")
        private String tenDangNhap;

        @Schema(description = "Full name", example = "John Doe")
        private String hoTen;

        @Schema(description = "Profile picture file ID", example = "1")
        private Long anhDaiDienId;
    }
}
