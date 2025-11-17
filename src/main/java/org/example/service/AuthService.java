package org.example.service;

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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final long DEFAULT_ROLE_ID = 2L;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;
    private final AuthMapper authMapper;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public AuthResponse login(AuthLoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email hoặc mật khẩu không chính xác"));

        if (Boolean.FALSE.equals(user.getActive())) {
            throw new IllegalStateException("Tài khoản đã bị khóa, vui lòng liên hệ quản trị viên");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không chính xác");
        }

        return buildAuthResponse(user);
    }

    @Transactional
    public AuthResponse register(AuthRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng");
        }

        Role role = roleRepository.findById(DEFAULT_ROLE_ID)
                .orElseThrow(() -> new IllegalArgumentException("Role mặc định không tồn tại"));

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = authMapper.toUser(request, encodedPassword, role);

        User savedUser = userRepository.save(newUser);
        return buildAuthResponse(savedUser);
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

