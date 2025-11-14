package org.example.mapper;

import org.example.dto.response.file.FileUploadResponse;
import org.example.dto.response.user.UserResponse;
import org.example.entity.FileUpload;
import org.example.entity.User;
import org.springframework.stereotype.Component;

/**
 * Mapper xử lý các thao tác chuyển đổi liên quan tới User entity
 */
@Component
public class UserMapper {

    /**
     * Tạo mới User entity từ các thông tin đầu vào đã được validate/encode
     *
     * @param email           Email của user
     * @param encodedPassword Password đã được mã hóa
     * @param fullName        Họ tên đầy đủ
     * @return User entity đã khởi tạo với giá trị mặc định phù hợp
     */
    public User toNewUser(String email, String encodedPassword, String fullName) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setFullName(fullName);
        user.setActive(true);
        return user;
    }

    /**
     * Áp dụng các giá trị mặc định cần thiết trước khi lưu user
     *
     * @param user User entity cần chuẩn hóa
     */
    public void applyDefaults(User user) {
        if (user.getActive() == null) {
            user.setActive(true);
        }
    }

    /**
     * Chuyển đổi User entity sang UserResponse DTO
     *
     * @param user User entity
     * @return UserResponse DTO
     */
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setAvatar(toFileUploadResponse(user.getAvatar()));
        response.setActive(user.getActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    /**
     * Chuyển đổi FileUpload entity sang FileUploadResponse DTO
     *
     * @param fileUpload FileUpload entity
     * @return FileUploadResponse DTO, null nếu fileUpload là null
     */
    public FileUploadResponse toFileUploadResponse(FileUpload fileUpload) {
        if (fileUpload == null) {
            return null;
        }
        FileUploadResponse response = new FileUploadResponse();
        response.setId(fileUpload.getId());
        response.setType(fileUpload.getType() != null ? fileUpload.getType().name() : null);
        response.setMimeType(fileUpload.getMimeType());
        response.setFileName(fileUpload.getFileName());
        response.setPath(fileUpload.getPath());
        response.setSize(fileUpload.getSize());
        response.setWidth(fileUpload.getWidth());
        response.setHeight(fileUpload.getHeight());
        response.setCreatedAt(fileUpload.getCreatedAt());
        return response;
    }
}

