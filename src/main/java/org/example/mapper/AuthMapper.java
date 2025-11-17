package org.example.mapper;

import org.example.dto.request.auth.AuthRegisterRequest;
import org.example.entity.Role;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public User toUser(AuthRegisterRequest request, String encodedPassword, Role role) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAvatar(null);
        user.setRole(role);
        user.setActive(true);
        return user;
    }
}
