package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.auth.AuthForgotPasswordRequest;
import org.example.dto.request.auth.AuthLoginRequest;
import org.example.dto.request.auth.AuthRegisterRequest;
import org.example.dto.response.auth.AuthResponse;
import org.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "API quản lý đăng nhập và đăng ký")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Đăng nhập", description = "Đăng nhập vào hệ thống")
    @ApiResponse(responseCode = "200", description = "Đăng nhập thành công")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Đăng ký", description = "Đăng ký tài khoản mới. Email xác thực sẽ được gửi đến email đăng ký.")
    @ApiResponse(responseCode = "200", description = "Đăng ký thành công, vui lòng kiểm tra email để xác thực")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody AuthRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
    
    @GetMapping("/verify-email")
    @Operation(summary = "Xác thực email", description = "Xác thực email đăng ký thông qua token trong link email")
    @ApiResponse(responseCode = "200", description = "Xác thực email thành công")
    public ResponseEntity<Map<String, String>> verifyEmail(@org.springframework.web.bind.annotation.RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Quên mật khẩu", description = "Quên mật khẩu và gửi email để đặt lại mật khẩu")
    @ApiResponse(responseCode = "200", description = "Quên mật khẩu thành công")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody AuthForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công. Vui lòng đăng nhập lại."));
    }
}
