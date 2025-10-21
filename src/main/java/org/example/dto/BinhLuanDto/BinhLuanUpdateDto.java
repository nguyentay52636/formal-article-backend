package org.example.dto.BinhLuanDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Update DTO for comment information")
public class BinhLuanUpdateDto {

    @Size(max = 2000, message = "Comment content must not exceed 2000 characters")
    @Schema(description = "Comment content", example = "Updated comment content")
    private String noiDung;

    @Size(max = 100, message = "Guest name must not exceed 100 characters")
    @Schema(description = "Guest name", example = "John Doe")
    private String tenKhach;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Schema(description = "Guest email", example = "john.doe@example.com")
    private String emailKhach;

    @Schema(description = "Comment status", example = "active")
    private String trangThai;

    @Schema(description = "Additional information", example = "Updated additional details")
    private String thongTinBoSung;
}
