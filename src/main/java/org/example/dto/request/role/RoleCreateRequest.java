package org.example.dto.request.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho request tạo role mới
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {
    
    @NotBlank(message = "Tên role không được để trống")
    @Size(max = 150, message = "Tên role không được vượt quá 150 ký tự")
    private String name;
    
    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;
}

