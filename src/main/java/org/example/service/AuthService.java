package org.example.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.config.JwtConfig;
import org.example.dto.request.auth.AuthForgotPasswordRequest;
import org.example.dto.request.auth.AuthLoginRequest;
import org.example.dto.request.auth.AuthRegisterRequest;
import org.example.dto.response.auth.AuthResponse;
import org.example.dto.response.user.UserResponse;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.mapper.AuthMapper;
import org.example.mapper.UserMapper;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;
    private final EmailService emailService;


    @Transactional(readOnly = true)
    public AuthResponse login(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không chính xác"));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new IllegalStateException("Tài khoản đã bị khóa, vui lòng liên hệ quản trị viên");
        }
        
        if (Boolean.FALSE.equals(user.getEmailVerified())) {
            throw new IllegalStateException("Email chưa được xác thực. Vui lòng kiểm tra email và xác thực tài khoản trước khi đăng nhập.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không chính xác");
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public Map<String, String> register(AuthRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng");
        }

        // Tìm role USER (không phụ thuộc vào ID, đảm bảo luôn là role USER)
        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalArgumentException("Role USER không tồn tại trong hệ thống. Vui lòng liên hệ quản trị viên."));

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = authMapper.toUser(request, encodedPassword, role);
        
        // User mới chưa active và chưa verified email
        newUser.setActive(false);
        newUser.setEmailVerified(false);
        
        // Đảm bảo role là USER (không phải ADMIN)
        newUser.setRole(role);
        
        // Tạo token xác thực email
        String verificationToken = jwtTokenProvider.generateEmailVerificationToken(newUser.getEmail());
        newUser.setEmailVerificationToken(verificationToken);

        User savedUser = userRepository.save(newUser);
        
        // Log để kiểm tra role được gán
        log.info("User {} đã đăng ký với role: {} (ID: {}), email xác thực đã được gửi", 
                savedUser.getEmail(), 
                savedUser.getRole().getName(), 
                savedUser.getRole().getId());
        
        // Gửi email xác thực
        emailService.sendEmailVerification(savedUser, verificationToken);
        
        // KHÔNG trả về token - user phải verify email trước khi đăng nhập
        return Map.of(
            "message", "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.",
            "email", savedUser.getEmail()
        );
    }
    
    @Transactional
    public Map<String, String> verifyEmail(String token) {
        try {
            // Validate token
            if (!jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("Token xác thực không hợp lệ hoặc đã hết hạn");
            }
            
            String email = jwtTokenProvider.getUsernameFromToken(token);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));
            
            // Kiểm tra token có khớp không
            if (!token.equals(user.getEmailVerificationToken())) {
                throw new IllegalArgumentException("Token xác thực không hợp lệ");
            }
            
            // Kiểm tra đã verify chưa
            if (Boolean.TRUE.equals(user.getEmailVerified())) {
                return Map.of(
                    "message", "Email đã được xác thực trước đó",
                    "verified", "true"
                );
            }
            
            // Kích hoạt tài khoản
            user.setEmailVerified(true);
            user.setActive(true);
            user.setEmailVerificationToken(null); // Xóa token sau khi verify
            userRepository.save(user);
            
            log.info("User {} đã xác thực email thành công", user.getEmail());
            
            return Map.of(
                "message", "Xác thực email thành công! Bạn có thể đăng nhập ngay bây giờ.",
                "verified", "true"
            );
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Token xác thực đã hết hạn. Vui lòng đăng ký lại.");
        } catch (Exception e) {
            log.error("Lỗi xác thực email: {}", e.getMessage(), e);
            throw new IllegalArgumentException("Không thể xác thực email: " + e.getMessage());
        }
    }


    @Transactional
    public void forgotPassword(AuthForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với email đã cung cấp"));

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        log.info("User {} đã cập nhật mật khẩu thông qua chức năng quên mật khẩu", user.getEmail());
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateTokenFromUsername(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());
        UserResponse userResponse = userMapper.toUserResponse(user);

        return AuthResponse.builder()
                .tokenType(jwtConfig.getTokenPrefix().trim())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtConfig.getExpiration())
                .refreshExpiresIn(jwtConfig.getRefreshExpiration())
                .user(userResponse)
                .build();
    }
}

