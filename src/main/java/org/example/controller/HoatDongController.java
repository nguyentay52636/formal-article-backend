package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.HoatDongDto.HoatDongCreateDto;
import org.example.dto.HoatDongDto.HoatDongResponseDto;
import org.example.dto.HoatDongDto.HoatDongUpdateDto;
import org.example.service.HoatDongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hoatdong")
@Tag(name = "Hoat Dong API", description = "APIs for managing activity logs (LichSuHoatDong)")
@CrossOrigin(origins = "*")
public class HoatDongController {

    @Autowired
    private HoatDongService hoatDongService;

    @Operation(
            summary = "Create a new activity log",
            description = "Creates a new activity log entry"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Activity log created successfully",
                    content = @Content(schema = @Schema(implementation = HoatDongResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<?> createHoatDong(@Valid @RequestBody HoatDongCreateDto createDto) {
        try {
            HoatDongResponseDto responseDto = hoatDongService.createHoatDong(createDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create activity log: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get activity log by ID",
            description = "Retrieves detailed information about a specific activity log including user information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Activity log found",
                    content = @Content(schema = @Schema(implementation = HoatDongResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Activity log not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getHoatDongById(@PathVariable Long id) {
        try {
            Optional<HoatDongResponseDto> responseDto = hoatDongService.getHoatDongByIdWithUser(id);
            if (responseDto.isPresent()) {
                return new ResponseEntity<>(responseDto.get(), HttpStatus.OK);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Activity log not found with ID: " + id);
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve activity log: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Update activity log",
            description = "Updates an existing activity log"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Activity log updated successfully",
                    content = @Content(schema = @Schema(implementation = HoatDongResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Activity log not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateHoatDong(@PathVariable Long id, @Valid @RequestBody HoatDongUpdateDto updateDto) {
        try {
            Optional<HoatDongResponseDto> responseDto = hoatDongService.updateHoatDong(id, updateDto);
            if (responseDto.isPresent()) {
                return new ResponseEntity<>(responseDto.get(), HttpStatus.OK);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Activity log not found with ID: " + id);
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update activity log: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Delete specific activity log",
            description = "Deletes a specific activity log entry"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity log deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Activity log not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHoatDong(@PathVariable Long id) {
        try {
            boolean deleted = hoatDongService.deleteHoatDong(id);
            if (deleted) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Activity log deleted successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Activity log not found with ID: " + id);
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete activity log: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Delete all activity logs",
            description = "Deletes all activity logs (admin only - use with caution)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All activity logs deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - admin only")
    })
    @DeleteMapping
    public ResponseEntity<?> deleteAllHoatDong() {
        try {
            // Note: This should be protected by admin role in production
            hoatDongService.deleteAllHoatDong();
            Map<String, String> response = new HashMap<>();
            response.put("message", "All activity logs deleted successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete all activity logs: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get activity logs by user ID",
            description = "Retrieves all activity logs for a specific user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Activity logs retrieved successfully"
            ),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getHoatDongByUser(@PathVariable Long userId) {
        try {
            List<HoatDongResponseDto> activities = hoatDongService.getHoatDongByNguoiThucHien(userId);
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve user activity logs: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Log document download activity",
            description = "Logs when a user downloads a document"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Download activity logged successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/tai-tai-lieu")
    public ResponseEntity<?> logTaiTaiLieu(
            @RequestParam Long nguoiThucHienId,
            @RequestParam Long taiLieuId,
            @RequestParam String tenTaiLieu,
            @RequestParam(required = false) String thongTinBoSung) {
        try {
            hoatDongService.logActivity(
                nguoiThucHienId,
                "TaiLieu",
                taiLieuId,
                "TAI_TAI_LIEU",
                null,
                "Downloaded: " + tenTaiLieu,
                thongTinBoSung
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Document download activity logged successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to log download activity: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Log comment activity",
            description = "Logs when a user comments on an article"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment activity logged successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/binh-luan")
    public ResponseEntity<?> logBinhLuan(
            @RequestParam Long nguoiThucHienId,
            @RequestParam Long baiVietId,
            @RequestParam Long binhLuanId,
            @RequestParam String noiDung,
            @RequestParam(required = false) String thongTinBoSung) {
        try {
            hoatDongService.logActivity(
                nguoiThucHienId,
                "BaiViet",
                baiVietId,
                "BINH_LUAN",
                null,
                "Commented: " + noiDung.substring(0, Math.min(noiDung.length(), 100)) + "...",
                thongTinBoSung
            );
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment activity logged successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to log comment activity: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Get activity statistics",
            description = "Gets statistics about activities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    @GetMapping("/stats")
    public ResponseEntity<?> getActivityStats(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String doiTuong,
            @RequestParam(required = false) String hanhDong) {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            if (userId != null) {
                stats.put("userActivityCount", hoatDongService.countHoatDongByNguoiThucHien(userId));
            }
            
            if (doiTuong != null && userId != null) {
                stats.put("userObjectActivityCount", hoatDongService.countHoatDongByDoiTuong(doiTuong, userId));
            }
            
            if (hanhDong != null) {
                stats.put("actionCount", hoatDongService.countHoatDongByHanhDong(hanhDong));
            }
            
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to retrieve statistics: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}