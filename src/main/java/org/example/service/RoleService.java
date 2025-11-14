package org.example.service;

import org.example.dto.request.role.RoleCreateRequest;
import org.example.dto.request.role.RoleUpdateRequest;
import org.example.dto.response.role.RoleResponse;
import org.example.entity.Role;
import org.example.mapper.RoleMapper;
import org.example.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service xử lý business logic cho Role
 */
@Service
@Transactional
public class RoleService {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private RoleMapper roleMapper;
    
    /**
     * Lấy role theo ID
     * @param id ID của role
     * @return RoleResponse nếu tìm thấy, null nếu không tìm thấy
     */
    public RoleResponse getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.map(roleMapper::toRoleResponse)
                   .orElse(null);
    }
    
    /**
     * Lấy tất cả roles
     * @return Danh sách tất cả roles
     */
    public List<RoleResponse> getAllRole() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                    .map(roleMapper::toRoleResponse)
                    .collect(Collectors.toList());
    }
    
    /**
     * Tạo role mới
     * @param request DTO chứa thông tin role cần tạo
     * @return RoleResponse của role vừa tạo
     * @throws RuntimeException nếu tên role đã tồn tại
     */
    public RoleResponse createRole(RoleCreateRequest request) {
        // Kiểm tra name đã tồn tại chưa
        if (roleRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên role đã tồn tại: " + request.getName());
        }
        
        // Tạo đối tượng Role mới thông qua mapper
        Role role = roleMapper.toRole(request);
        
        // Lưu vào database
        Role savedRole = roleRepository.save(role);
        
        return roleMapper.toRoleResponse(savedRole);
    }
    
    /**
     * Cập nhật role
     * @param id ID của role cần cập nhật
     * @param request DTO chứa thông tin cập nhật
     * @return RoleResponse của role đã được cập nhật
     * @throws RuntimeException nếu role không tồn tại hoặc tên role đã tồn tại
     */
    public RoleResponse updateRole(Long id, RoleUpdateRequest request) {
        // Kiểm tra role có tồn tại không
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại với ID: " + id));
        
        // Kiểm tra nếu tên mới khác tên cũ và đã tồn tại
        if (!role.getName().equals(request.getName()) && roleRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên role đã tồn tại: " + request.getName());
        }
        
        // Cập nhật thông tin thông qua mapper
        roleMapper.updateRole(request, role);
        
        // Lưu vào database
        Role updatedRole = roleRepository.save(role);
        
        return roleMapper.toRoleResponse(updatedRole);
    }
    
    /**
     * Xóa role
     * @param id ID của role cần xóa
     * @throws RuntimeException nếu role không tồn tại
     */
    public void deleteRole(Long id) {
        // Kiểm tra role có tồn tại không
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role không tồn tại với ID: " + id);
        }
        
        // Xóa role
        roleRepository.deleteById(id);
    }
    
}

