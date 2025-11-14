package org.example.service;

import org.example.dto.request.user.UserCreateRequest;
import org.example.dto.request.user.UserUpdateRequest;
import org.example.dto.response.user.UserResponse;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Để mã hóa password
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * Tạo user mới từ UserCreateRequest
     * @param request UserCreateRequest
     * @return UserResponse của user vừa tạo
     */
    public UserResponse createUser(UserCreateRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + request.getEmail());
        }
        
        // Kiểm tra phone đã tồn tại chưa
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + request.getPhone());
        }
        
        // Tìm role theo ID, mặc định là 1 (ADMIN) nếu không được truyền
        Long roleId = 1L; // Mặc định role ADMIN
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role không tồn tại với ID: " + roleId));
        
        // Mã hóa password
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        
        // Xử lý avatar: nếu null hoặc empty thì set thành null
        String avatar = (request.getAvatar() != null && !request.getAvatar().trim().isEmpty()) 
                ? request.getAvatar() 
                : null;
        
        // Tạo đối tượng User mới thông qua mapper
        User user = userMapper.toUser(request, encodedPassword, role, avatar);
        
        // Lưu vào database
        User savedUser = userRepository.save(user);
        
        return userMapper.toUserResponse(savedUser);
    }
    
    /**
     * Tạo user mới (cách 2: truyền đối tượng User)
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + user.getEmail());
        }
        
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + user.getPhone());
        }
        
        // Mã hóa password nếu chưa được mã hóa
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        // Áp dụng giá trị mặc định thông qua mapper
        userMapper.applyDefaults(user);
        
        return userRepository.save(user);
    }
    
    /**
     * Lấy tất cả users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Lấy tất cả users dưới dạng UserResponse
     */
    public List<UserResponse> getAllUsersResponse() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(userMapper::toUserResponse)
                    .collect(Collectors.toList());
    }
    
    /**
     * Tìm user theo ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    /**
     * Tìm user theo ID và trả về UserResponse
     */
    public UserResponse getUserResponseById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::toUserResponse).orElse(null);
    }
    
    /**
     * Tìm user theo email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Cập nhật user từ UserUpdateRequest
     * @param id ID của user cần cập nhật
     * @param request UserUpdateRequest
     * @return UserResponse của user đã được cập nhật
     */
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        // Tìm user theo ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User không tồn tại với ID: " + id));
        
        // Kiểm tra email đã tồn tại chưa (nếu thay đổi email)
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + request.getEmail());
        }
        
        // Kiểm tra phone đã tồn tại chưa (nếu thay đổi phone)
        if (!user.getPhone().equals(request.getPhone()) && userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + request.getPhone());
        }
        
        // Tìm role nếu có roleId trong request
        Role role = null;
        if (request.getRoleId() != null) {
            role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại với ID: " + request.getRoleId()));
        }
        
        // Cập nhật thông tin user thông qua mapper
        userMapper.updateUser(request, user, role);
        
        // Lưu vào database
        User updatedUser = userRepository.save(user);
        
        return userMapper.toUserResponse(updatedUser);
    }
    
    /**
     * Cập nhật user (cách 2: truyền đối tượng User)
     */
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User không tồn tại với ID: " + user.getId());
        }
        
        // Kiểm tra phone unique nếu thay đổi
        User existingUser = userRepository.findById(user.getId()).orElse(null);
        if (existingUser != null && !existingUser.getPhone().equals(user.getPhone()) 
            && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại: " + user.getPhone());
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Xóa user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

