package org.example.dto.KhoiNoiBat;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhoiNoiBatUpdateDto {

    @Size(max = 50, message = "Mã khối nổi bật không được vượt quá 50 ký tự")
    private String ma;

    @Size(max = 100, message = "Tên khối nổi bật không được vượt quá 100 ký tự")
    private String ten;

    private String cauHinh;

    private Boolean kichHoat;
}
