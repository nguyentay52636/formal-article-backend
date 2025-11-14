package org.example.service;

import org.example.dto.response.user.UserResponse;
import org.example.entity.User;
import org.example.mapper.UserMapper;
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
    private PasswordEncoder passwordEncoder; // Để mã hóa password
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * Tạo user mới
     * @param email Email của user
     * @param password Password (sẽ được mã hóa)
     * @param fullName Tên đầy đủ
     * @return User đã được lưu vào database
     */
    public User createUser(String email, String password, String fullName) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại: " + email);
        }
        
        // Tạo đối tượng User mới thông qua mapper
        String encodedPassword = passwordEncoder.encode(password);
        User user = userMapper.toNewUser(email, encodedPassword, fullName);
        
        // Lưu vào database
        return userRepository.save(user);
    }
    
    /**
     * Tạo user mới và trả về UserResponse
     * @param email Email của user
     * @param password Password (sẽ được mã hóa)
     * @param fullName Tên đầy đủ
     * @return UserResponse của user vừa tạo
     */
    public UserResponse createUserResponse(String email, String password, String fullName) {
        User user = createUser(email, password, fullName);
        return userMapper.toUserResponse(user);
    }
    
    /**
     * Tạo user mới (cách 2: truyền đối tượng User)
     */
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + user.getEmail());
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
     * Cập nhật user
     */
    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new RuntimeException("User không tồn tại với ID: " + user.getId());
        }
        return userRepository.save(user);
    }
    
    /**
     * Cập nhật user và trả về UserResponse
     */
    public UserResponse updateUserResponse(User user) {
        User updatedUser = updateUser(user);
        return userMapper.toUserResponse(updatedUser);
    }
    
    /**
     * Xóa user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

