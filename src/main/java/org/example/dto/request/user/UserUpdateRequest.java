package org.example.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO cho request cập nhật thông tin User
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 150, message = "Email không được vượt quá 150 ký tự")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải có đúng 10 chữ số")
    private String phone;

    @Size(max = 150, message = "Họ tên không được vượt quá 150 ký tự")
    private String fullName;

    @Size(max = 500, message = "Avatar URL không được vượt quá 500 ký tự")
    private String avatar;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean active;

    private Long roleId;
}

