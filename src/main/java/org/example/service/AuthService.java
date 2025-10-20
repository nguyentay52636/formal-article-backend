package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.AuthDto.AuthRequest;
import org.example.dto.AuthDto.AuthResponse;
import org.example.dto.AuthDto.ForgotPasswordRequest;
import org.example.dto.AuthDto.ForgotPasswordResponse;
import org.example.dto.AuthDto.RegisterRequest;
import org.example.dto.AuthDto.ResetPasswordRequest;
import org.example.dto.AuthDto.ResetPasswordResponse;
import org.example.entity.NguoiDung;
import org.example.entity.PasswordResetCode;
import org.example.entity.VaiTro;
import org.example.repository.NguoiDungRepository;
import org.example.repository.PasswordResetCodeRepository;
import org.example.repository.VaiTroRepository;
import org.example.security.JwtTokenProvider;
import org.example.service.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final NguoiDungRepository nguoiDungRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordResetCodeRepository passwordResetCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final EmailService emailService;

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

    @Transactional
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        log.info("Processing forgot password request for email: {}", request.getEmail());

        // Tìm người dùng theo email
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));

        // Kiểm tra rate limiting - không cho phép gửi quá nhiều email trong 5 phút
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        long recentCodes = passwordResetCodeRepository.countRecentUnusedCodes(request.getEmail(), fiveMinutesAgo);
        
        if (recentCodes >= 3) {
            throw new RuntimeException("Bạn đã gửi quá nhiều yêu cầu đặt lại mật khẩu. Vui lòng đợi 5 phút trước khi thử lại.");
        }

        // Xóa các mã cũ đã hết hạn
        passwordResetCodeRepository.deleteExpiredCodes(LocalDateTime.now());

        // Tạo mã reset ngẫu nhiên 6 chữ số
        String resetCode = generateResetCode();

        // Lưu mã reset vào database
        PasswordResetCode passwordResetCode = new PasswordResetCode();
        passwordResetCode.setEmail(request.getEmail());
        passwordResetCode.setResetCode(resetCode);
        passwordResetCodeRepository.save(passwordResetCode);

        try {
            // Gửi email với mã reset (test mode)
            emailService.sendForgotPasswordEmailTest(
                    nguoiDung.getEmail(),
                    nguoiDung.getHoTen(),
                    resetCode
            );

            log.info("Reset password email sent successfully to: {}", request.getEmail());

            return ForgotPasswordResponse.builder()
                    .message("Email đặt lại mật khẩu đã được gửi thành công. Vui lòng kiểm tra hộp thư của bạn.")
                    .success(true)
                    .email(request.getEmail())
                    .build();

        } catch (Exception e) {
            log.error("Failed to send reset password email to: {}", request.getEmail(), e);
            throw new RuntimeException("Không thể gửi email đặt lại mật khẩu. Vui lòng thử lại sau.");
        }
    }

    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        log.info("Processing reset password request for email: {}", request.getEmail());

        // Validate password confirmation
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp");
        }

        // Tìm người dùng theo email
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản với email này"));

        // Tìm mã reset hợp lệ
        PasswordResetCode resetCode = passwordResetCodeRepository
                .findByEmailAndResetCodeAndUsedFalseAndExpiresAtAfter(
                        request.getEmail(), 
                        request.getResetCode(), 
                        LocalDateTime.now()
                )
                .orElseThrow(() -> new RuntimeException("Mã xác thực không hợp lệ hoặc đã hết hạn"));

        // Đánh dấu mã đã sử dụng
        passwordResetCodeRepository.markAsUsed(request.getEmail(), request.getResetCode());

        // Cập nhật mật khẩu mới
        nguoiDung.setMatKhau(passwordEncoder.encode(request.getNewPassword()));
        nguoiDungRepository.save(nguoiDung);

        log.info("Password reset successfully for email: {}", request.getEmail());

        return ResetPasswordResponse.builder()
                .message("Đặt lại mật khẩu thành công. Bạn có thể đăng nhập với mật khẩu mới.")
                .success(true)
                .email(request.getEmail())
                .build();
    }

    private String generateResetCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Tạo số 6 chữ số từ 100000 đến 999999
        return String.valueOf(code);
    }
}
