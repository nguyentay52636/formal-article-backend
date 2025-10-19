package org.example.dto.VaiTroDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "VaiTro Response DTO")
public class VaiTroResponseDTO {

    @Schema(description = "ID vai trò", example = "1")
    private Long id;

    @Schema(description = "Mã vai trò", example = "quan_tri")
    private String ma;

    @Schema(description = "Tên vai trò", example = "quan_tri")
    private String ten;
}
