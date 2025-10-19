package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthDto.AuthRequest;
import org.example.dto.AuthDto.AuthResponse;
import org.example.dto.AuthDto.RegisterRequest;
import org.example.entity.NguoiDung;
import org.example.entity.VaiTro;
import org.example.repository.NguoiDungRepository;
import org.example.repository.VaiTroRepository;
import org.example.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse login(AuthRequest request) {
        log.info("Attempting to authenticate user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());

        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000L) // 24 hours
                .userInfo(AuthResponse.UserInfo.builder()
                        .id(nguoiDung.getId())
                        .username(nguoiDung.getTenDangNhap())
                        .email(nguoiDung.getEmail())
                        .hoTen(nguoiDung.getHoTen())
                        .role(nguoiDung.getVaiTro() != null ? nguoiDung.getVaiTro().getMa() : "USER")
                        .build())
                .build();
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Attempting to register new user: {}", request.getUsername());

        // Check if username already exists
        if (nguoiDungRepository.existsByTenDangNhap(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // Check if email already exists
        if (nguoiDungRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // Get default role (doc_gia)
        VaiTro defaultRole = vaiTroRepository.findByMa("doc_gia")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // Create new user
        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setTenDangNhap(request.getUsername());
        nguoiDung.setMatKhau(passwordEncoder.encode(request.getPassword()));
        nguoiDung.setEmail(request.getEmail());
        nguoiDung.setHoTen(request.getHoTen());
        nguoiDung.setVaiTro(defaultRole);
        nguoiDung.setTrangThai("ACTIVE");
        nguoiDung.setNgayTao(LocalDateTime.now());

        nguoiDung = nguoiDungRepository.save(nguoiDung);

        // Authenticate the user after registration
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .userInfo(AuthResponse.UserInfo.builder()
                        .id(nguoiDung.getId())
                        .username(nguoiDung.getTenDangNhap())
                        .email(nguoiDung.getEmail())
                        .hoTen(nguoiDung.getHoTen())
                        .role(nguoiDung.getVaiTro() != null ? nguoiDung.getVaiTro().getMa() : "doc_gia")
                        .build())
                .build();
    }

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Attempting to refresh token");

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = tokenProvider.getUsernameFromToken(refreshToken);
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = tokenProvider.generateTokenFromUsername(username);
        String newRefreshToken = tokenProvider.generateRefreshToken(username);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(86400000L)
                .userInfo(AuthResponse.UserInfo.builder()
                        .id(nguoiDung.getId())
                        .username(nguoiDung.getTenDangNhap())
                        .email(nguoiDung.getEmail())
                        .hoTen(nguoiDung.getHoTen())
                        .role(nguoiDung.getVaiTro() != null ? nguoiDung.getVaiTro().getMa() : "USER")
                        .build())
                .build();
    }
}
