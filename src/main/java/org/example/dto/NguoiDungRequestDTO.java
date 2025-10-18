package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new user")
public class NguoiDungRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Username for login", example = "johndoe123", required = true)
    private String tenDangNhap;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be at least 6 characters")
    @Schema(description = "User password", example = "SecurePass123!", required = true)
    private String matKhau;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "User email address", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String hoTen;

    @Schema(description = "Profile picture file ID", example = "1")
    private Long anhDaiDienId;

    @Schema(description = "Gender", example = "Nam")
    private String gioiTinh;

    @Schema(description = "Date of birth", example = "1990-01-15T00:00:00")
    private LocalDateTime ngaySinh;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    @Schema(description = "User address", example = "123 Main St, City")
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
