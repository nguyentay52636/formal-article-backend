package org.example.mapper;

import org.example.dto.request.user.UserCreateRequest;
import org.example.dto.request.user.UserUpdateRequest;
import org.example.dto.response.role.RoleResponse;
import org.example.dto.response.user.UserResponse;
import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper xử lý các thao tác chuyển đổi liên quan tới User entity
 */
@Component
public class UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * Tạo mới User entity từ các thông tin đầu vào đã được validate/encode
     *
     * @param email           Email của user
     * @param encodedPassword Password đã được mã hóa
     * @param fullName        Họ tên đầy đủ
     * @param phone           Số điện thoại
     * @param avatar          URL avatar
     * @param role            Role entity
     * @return User entity đã khởi tạo với giá trị mặc định phù hợp
     */
    public User toNewUser(String email, String encodedPassword, String fullName, String phone, String avatar, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setAvatar(avatar);
        user.setRole(role);
        user.setActive(true);
        return user;
    }

    /**
     * Tạo User entity từ UserCreateRequest
     *
     * @param request UserCreateRequest
     * @param encodedPassword Password đã được mã hóa
     * @param role            Role entity
     * @param avatar          Avatar URL (có thể null)
     * @return User entity
     */
    public User toUser(UserCreateRequest request, String encodedPassword, Role role, String avatar) {
        if (request == null) {
            return null;
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAvatar(avatar); // Sử dụng avatar đã được xử lý từ service
        user.setRole(role);
        user.setActive(true);
        return user;
    }

    /**
     * Cập nhật User entity từ UserUpdateRequest
     *
     * @param request UserUpdateRequest
     * @param user    User entity cần cập nhật
     * @param role    Role entity (có thể null nếu không cập nhật role)
     */
    public void updateUser(UserUpdateRequest request, User user, Role role) {
        if (request == null || user == null) {
            return;
        }
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setFullName(request.getFullName());
        user.setAvatar(request.getAvatar());
        user.setActive(request.getActive());
        if (role != null) {
            user.setRole(role);
        }
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
        response.setPhone(user.getPhone());
        response.setAvatar(user.getAvatar());
        response.setActive(user.getActive());
        response.setRole(roleMapper.toRoleResponse(user.getRole()));
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }
}

