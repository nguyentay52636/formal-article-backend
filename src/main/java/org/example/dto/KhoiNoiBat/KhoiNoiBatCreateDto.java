package org.example.dto.KhoiNoiBat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoiNoiBatCreateDto {

    @NotBlank(message = "Mã khối nổi bật không được để trống")
    @Size(max = 50, message = "Mã khối nổi bật không được vượt quá 50 ký tự")
    private String ma;

    @NotBlank(message = "Tên khối nổi bật không được để trống")
    @Size(max = 100, message = "Tên khối nổi bật không được vượt quá 100 ký tự")
    private String ten;

    private String cauHinh;

    private Boolean kichHoat = true;
}
