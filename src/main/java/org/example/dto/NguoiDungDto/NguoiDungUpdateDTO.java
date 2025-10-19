package org.example.dto.NguoiDungDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating user information")
public class NguoiDungUpdateDTO {

    @Size(min = 6, max = 255, message = "Password must be at least 6 characters")
    @Schema(description = "New password (optional)", example = "NewSecurePass123!")
    private String matKhau;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Schema(description = "Full name", example = "John Doe")
    private String hoTen;

    @Schema(description = "Profile picture file ID", example = "1")
    private Long anhDaiDienId;

    @Schema(description = "Gender", example = "Nam")
    private String gioiTinh;

    @Schema(description = "Date of birth", example = "1990-01-15T00:00:00")
    private LocalDateTime ngaySinh;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    @Schema(description = "Address", example = "123 Main St, City")
    private String diaChi;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Schema(description = "Phone number", example = "+84123456789")
    private String soDienThoai;

    @Schema(description = "User status", example = "active")
    private String trangThai;

    @Schema(description = "Additional information", example = "Additional user details")
    private String thongTinBoSung;

    @Schema(description = "Role ID", example = "1")
    private Long vaiTroId;
}
