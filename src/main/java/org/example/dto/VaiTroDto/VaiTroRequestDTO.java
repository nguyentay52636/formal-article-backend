package org.example.dto.VaiTroDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "VaiTro Request DTO")
public class VaiTroRequestDTO {

    @Schema(description = "Mã vai trò", example = "quan_tri", maxLength = 50)
    @NotBlank(message = "Mã vai trò không được để trống")
    @Size(max = 50, message = "Mã vai trò không được vượt quá 50 ký tự")
    private String ma;

    @Schema(description = "Tên vai trò", example = "quan_tri", 
            allowableValues = {"doc_gia", "tac_gia", "quan_tri", "bien_tap"})
    @NotBlank(message = "Tên vai trò không được để trống")
    @Pattern(regexp = "^(doc_gia|tac_gia|quan_tri|bien_tap)$", 
             message = "Tên vai trò chỉ được là: doc_gia, tac_gia, quan_tri, bien_tap")
    @Size(max = 100, message = "Tên vai trò không được vượt quá 100 ký tự")
    private String ten;
}
