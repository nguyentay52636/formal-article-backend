package org.example.controller;

import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.request.role.RoleCreateRequest;
import org.example.dto.request.role.RoleUpdateRequest;
import org.example.dto.response.role.RoleResponse;
import org.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Vai trò (Role)", description = "API quản lý vai trò (Role) trong hệ thống")
@RequiredArgsConstructor
public class RoleController {
    
    @Autowired
    private RoleService roleService;
    @GetMapping("/{id}")
    @Operation(summary = "Lấy role theo ID", description = "Trả về thông tin chi tiết của role dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tìm thấy role"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy role với ID đã cho")
    })
    public ResponseEntity<RoleResponse> getRoleById(
            @Parameter(description = "ID của role cần lấy", required = true)
            @PathVariable Long id) {
        RoleResponse role = roleService.getRoleById(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Lấy tất cả roles
     * GET /api/roles
     */
    @GetMapping
    @Operation(summary = "Lấy tất cả roles", description = "Trả về danh sách tất cả các roles trong hệ thống")
    @ApiResponse(responseCode = "200", description = "Danh sách roles")
    public ResponseEntity<List<RoleResponse>> getAllRole() {
        List<RoleResponse> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }
    
    /**
     * Tạo role mới
     * POST /api/roles
     */
    @PostMapping
    @Operation(summary = "Tạo role mới", description = "Tạo một role mới trong hệ thống")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Role đã được tạo thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc tên role đã tồn tại")
    })
    public ResponseEntity<?> createRole(
            @Parameter(description = "Thông tin role cần tạo", required = true)
            @RequestBody RoleCreateRequest request) {
        try {
            RoleResponse role = roleService.createRole(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(role);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * Cập nhật role
     * PUT /api/roles/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật role", description = "Cập nhật thông tin của role dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role đã được cập nhật thành công"),
        @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc tên role đã tồn tại"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy role với ID đã cho")
    })
    public ResponseEntity<?> updateRole(
            @Parameter(description = "ID của role cần cập nhật", required = true)
            @PathVariable Long id,
            @Parameter(description = "Thông tin role cần cập nhật", required = true)
            @RequestBody RoleUpdateRequest request) {
        try {
            RoleResponse role = roleService.updateRole(id, request);
            return ResponseEntity.ok(role);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("không tồn tại")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * Xóa role
     * DELETE /api/roles/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa role", description = "Xóa role dựa trên ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Role đã được xóa thành công"),
        @ApiResponse(responseCode = "404", description = "Không tìm thấy role với ID đã cho")
    })
    public ResponseEntity<?> deleteRole(
            @Parameter(description = "ID của role cần xóa", required = true)
            @PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

