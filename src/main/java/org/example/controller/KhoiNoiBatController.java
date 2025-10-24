package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.KhoiNoiBat.KhoiNoiBatCreateDto;
import org.example.dto.KhoiNoiBat.KhoiNoiBatResponseDto;
import org.example.dto.KhoiNoiBat.KhoiNoiBatUpdateDto;
import org.example.service.KhoiNoiBatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/khoi-noi-bat")
@Tag(name = "KhoiNoiBat API", description = "APIs for managing featured blocks (KhoiNoiBat)")
@CrossOrigin(origins = "*")
public class KhoiNoiBatController {

    @Autowired
    private KhoiNoiBatService khoiNoiBatService;

    @Operation(
            summary = "Tạo khối nổi bật mới",
            description = "Tạo một khối nổi bật mới trong hệ thống. Mã và tên phải là duy nhất."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Tạo khối nổi bật thành công",
                    content = @Content(schema = @Schema(implementation = KhoiNoiBatResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Mã hoặc tên đã tồn tại")
    })
    @PostMapping
    public ResponseEntity<?> createKhoiNoiBat(
            @Valid @RequestBody KhoiNoiBatCreateDto requestDTO) {
        try {
            KhoiNoiBatResponseDto responseDTO = khoiNoiBatService.createKhoiNoiBat(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
    }

    @Operation(
            summary = "Lấy danh sách tất cả khối nổi bật",
            description = "Lấy danh sách tất cả khối nổi bật trong hệ thống"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy danh sách thành công",
                    content = @Content(schema = @Schema(implementation = KhoiNoiBatResponseDto.class))
            )
    })
    @GetMapping
    public ResponseEntity<List<KhoiNoiBatResponseDto>> getAllKhoiNoiBat() {
        List<KhoiNoiBatResponseDto> khoiNoiBats = khoiNoiBatService.getAllKhoiNoiBat();
        return new ResponseEntity<>(khoiNoiBats, HttpStatus.OK);
    }


    @Operation(
            summary = "Xem chi tiết khối nổi bật",
            description = "Lấy thông tin chi tiết của một khối nổi bật theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy khối nổi bật thành công",
                    content = @Content(schema = @Schema(implementation = KhoiNoiBatResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khối nổi bật")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getKhoiNoiBatById(
            @Parameter(description = "ID khối nổi bật", example = "1")
            @PathVariable Long id) {
        try {
            KhoiNoiBatResponseDto responseDTO = khoiNoiBatService.getKhoiNoiBatById(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Cập nhật khối nổi bật",
            description = "Cập nhật thông tin của một khối nổi bật theo ID. Chỉ các trường được cung cấp mới được cập nhật."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Cập nhật khối nổi bật thành công",
                    content = @Content(schema = @Schema(implementation = KhoiNoiBatResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khối nổi bật"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu đầu vào không hợp lệ"),
            @ApiResponse(responseCode = "409", description = "Mã hoặc tên đã tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateKhoiNoiBat(
            @Parameter(description = "ID khối nổi bật", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody KhoiNoiBatUpdateDto updateDTO) {
        try {
            KhoiNoiBatResponseDto responseDTO = khoiNoiBatService.updateKhoiNoiBat(id, updateDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            if (e.getMessage().contains("Không tìm thấy")) {
                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            } else if (e.getMessage().contains("đã tồn tại")) {
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Bật/tắt kích hoạt",
            description = "Chuyển đổi trạng thái kích hoạt của khối nổi bật (true/false)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Chuyển đổi trạng thái thành công",
                    content = @Content(schema = @Schema(implementation = KhoiNoiBatResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khối nổi bật")
    })
    @PatchMapping("/{id}/kich-hoat")
    public ResponseEntity<?> toggleKichHoat(
            @Parameter(description = "ID khối nổi bật", example = "1")
            @PathVariable Long id) {
        try {
            KhoiNoiBatResponseDto responseDTO = khoiNoiBatService.toggleKichHoat(id);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Xóa khối nổi bật",
            description = "Xóa một khối nổi bật khỏi hệ thống theo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xóa khối nổi bật thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy khối nổi bật")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteKhoiNoiBat(
            @Parameter(description = "ID khối nổi bật", example = "1")
            @PathVariable Long id) {
        try {
            khoiNoiBatService.deleteKhoiNoiBat(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xóa khối nổi bật thành công");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Không tìm thấy khối nổi bật");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
