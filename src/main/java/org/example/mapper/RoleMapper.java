package org.example.mapper;

import org.example.dto.request.role.RoleCreateRequest;
import org.example.dto.request.role.RoleUpdateRequest;
import org.example.dto.response.role.RoleResponse;
import org.example.entity.Role;
import org.springframework.stereotype.Component;

/**
 * Mapper chuyển đổi giữa Role entity và các DTO liên quan
 */
@Component
public class RoleMapper {

    /**
     * Chuyển đổi Role entity sang RoleResponse
     *
     * @param role Role entity
     * @return RoleResponse DTO
     */
    public RoleResponse toRoleResponse(Role role) {
        if (role == null) {
            return null;
        }
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setCreatedAt(role.getCreatedAt());
        response.setUpdatedAt(role.getUpdatedAt());
        return response;
    }

    /**
     * Tạo Role entity từ RoleCreateRequest
     *
     * @param request RoleCreateRequest
     * @return Role entity
     */
    public Role toRole(RoleCreateRequest request) {
        if (request == null) {
            return null;
        }
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        return role;
    }

    /**
     * Cập nhật Role entity từ RoleUpdateRequest
     *
     * @param request RoleUpdateRequest
     * @param role    Role entity cần cập nhật
     */
    public void updateRole(RoleUpdateRequest request, Role role) {
        if (request == null || role == null) {
            return;
        }
        role.setName(request.getName());
        role.setDescription(request.getDescription());
    }
}

