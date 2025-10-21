package org.example.dto.BinhLuanDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for creating a new comment")
public class BinhLuanCreateDto {

    @NotNull(message = "Article ID is required")
    @Schema(description = "Article ID", example = "1", required = true)
    private Long baiVietId;

    @Schema(description = "User ID (optional for guest comments)", example = "1")
    private Long nguoiDungId;

    @NotBlank(message = "Comment content is required")
    @Size(max = 2000, message = "Comment content must not exceed 2000 characters")
    @Schema(description = "Comment content", example = "This is a great article!", required = true)
    private String noiDung;

    @Size(max = 100, message = "Guest name must not exceed 100 characters")
    @Schema(description = "Guest name (for anonymous comments)", example = "John Doe")
    private String tenKhach;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Guest email (for anonymous comments)", example = "john.doe@example.com")
    private String emailKhach;

    @Schema(description = "Parent comment ID (for replies)", example = "1")
    private Long binhLuanChaId;

    @Schema(description = "Comment status", example = "active")
    private String trangThai;

    @Schema(description = "Additional information", example = "Additional comment details")
    private String thongTinBoSung;
}
