package org.example.service;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // Để mã hóa password
    
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
        
        // Tạo đối tượng User mới
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Mã hóa password
        user.setFullName(fullName);
        user.setActive(true); // Mặc định active = true
        
        // Lưu vào database
        return userRepository.save(user);
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
        
        // Đảm bảo active = true nếu chưa set
        if (user.getActive() == null) {
            user.setActive(true);
        }
        
        return userRepository.save(user);
    }
    
    /**
     * Lấy tất cả users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Tìm user theo ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
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
     * Xóa user
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

