package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.response.notification.NotificationResponse;
import org.example.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Thông báo (Notification)", description = "API quản lý thông báo")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Lấy danh sách thông báo", description = "Lấy tất cả thông báo của user")
    @ApiResponse(responseCode = "200", description = "Thành công")
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(
            @Parameter(description = "ID của User", required = true) @RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Đánh dấu đã đọc", description = "Đánh dấu thông báo là đã đọc")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công"),
        @ApiResponse(responseCode = "404", description = "Thông báo không tồn tại")
    })
    public ResponseEntity<NotificationResponse> markAsRead(
            @Parameter(description = "ID của thông báo", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }
}
