package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.generatedCv.GeneratedCvCreateRequest;
import org.example.dto.response.generatedCv.GeneratedCvResponse;
import org.example.service.GeneratedCvManagementService;
import org.example.service.JasperCvService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller quản lý CV đã tạo
 * Base path: /api/generated-cvs
 */
@RestController
@RequestMapping("/api/generated-cvs")
@RequiredArgsConstructor
@Tag(name = "Generated CV", description = "API quản lý CV đã tạo từ template")
public class GeneratedCvController {

    private final GeneratedCvManagementService generatedCvManagementService;
    private final JasperCvService jasperCvService;

    // ==================== CREATE ====================

    @Operation(summary = "Tạo CV mới", 
               description = "Tạo CV từ template. Hỗ trợ 2 mode:\n" +
                       "1. AI Mode: Truyền prompt → AI tự generate dataJson, styleJson, htmlOutput\n" +
                       "2. Manual Mode: Truyền dataJson, styleJson, htmlOutput trực tiếp")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tạo CV thành công",
                     content = @Content(schema = @Schema(implementation = GeneratedCvResponse.class))),
        @ApiResponse(responseCode = "400", description = "Request không hợp lệ"),
        @ApiResponse(responseCode = "404", description = "User hoặc Template không tồn tại")
    })
    @PostMapping
    public ResponseEntity<?> createCV(@Valid @RequestBody GeneratedCvCreateRequest request) {
        GeneratedCvResponse response = generatedCvManagementService.createCV(request);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("message", "Tạo CV thành công");
        result.put("data", response);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ==================== READ ====================

    @Operation(summary = "Lấy tất cả CV", 
               description = "Lấy danh sách tất cả CV trong hệ thống")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công")
    })
    @GetMapping
    public ResponseEntity<?> getAllCVs() {
        List<GeneratedCvResponse> cvList = generatedCvManagementService.getAllCVs();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", cvList);
        response.put("total", cvList.size());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy danh sách CV của user", 
               description = "Lấy tất cả CV đã tạo của user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công")
    })
    @GetMapping("/my-cvs")
    public ResponseEntity<?> getMyCVs(
            @Parameter(description = "ID của user") 
            @RequestParam Long userId) {
        
        List<GeneratedCvResponse> cvList = generatedCvManagementService.getAllCVsByUserId(userId);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", cvList);
        response.put("total", cvList.size());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy chi tiết CV", 
               description = "Lấy thông tin chi tiết CV bao gồm HTML, JSON data và PDF URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Thành công",
                     content = @Content(schema = @Schema(implementation = GeneratedCvResponse.class))),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCVById(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        GeneratedCvResponse response = generatedCvManagementService.getCVById(id);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("data", response);
        
        return ResponseEntity.ok(result);
    }

    // ==================== PDF OPERATIONS ====================

    @Operation(summary = "Preview PDF", 
               description = "Render PDF tạm để preview, trả về byte[] - không lưu file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF rendered thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi khi render PDF")
    })
    @GetMapping(value = "/{id}/preview-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> previewPdf(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        byte[] pdfBytes = jasperCvService.previewPdf(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "cv_preview_" + id + ".pdf");
        headers.setContentLength(pdfBytes.length);
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @Operation(summary = "Lưu PDF", 
               description = "Render PDF thật và lưu vào storage. Cập nhật pdfUrl trong CV")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lưu PDF thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại"),
        @ApiResponse(responseCode = "500", description = "Lỗi khi lưu PDF")
    })
    @PostMapping("/{id}/save-pdf")
    public ResponseEntity<?> savePdf(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        String pdfUrl = jasperCvService.savePdf(id);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Lưu PDF thành công");
        response.put("pdfUrl", pdfUrl);
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Download PDF", 
               description = "Download PDF đã lưu. Nếu chưa có PDF, trả về lỗi 404")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Download thành công"),
        @ApiResponse(responseCode = "404", description = "CV hoặc PDF không tồn tại")
    })
    @GetMapping(value = "/{id}/download-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> downloadPdf(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        String pdfPath = jasperCvService.getPdfPath(id);
        
        if (pdfPath == null) {
            return ResponseEntity.notFound().build();
        }
        
        File file = new File(pdfPath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        Resource resource = new FileSystemResource(file);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "cv_" + id + ".pdf");
        
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // ==================== UPDATE ====================

    @Operation(summary = "Cập nhật CV", 
               description = "Cập nhật thông tin CV. Nếu có prompt mới sẽ regenerate content từ AI")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCV(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id,
            @Valid @RequestBody GeneratedCvCreateRequest request) {
        GeneratedCvResponse response = generatedCvManagementService.updateCV(id, request);
        
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("status", "success");
        result.put("message", "Cập nhật CV thành công");
        result.put("data", response);
        
        return ResponseEntity.ok(result);
    }

    // ==================== DELETE ====================

    @Operation(summary = "Xóa CV", 
               description = "Xóa CV đã tạo (bao gồm cả file PDF nếu có)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Xóa thành công"),
        @ApiResponse(responseCode = "404", description = "CV không tồn tại")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCV(
            @Parameter(description = "ID của CV") 
            @PathVariable Long id) {
        // Xóa PDF trước
        jasperCvService.deletePdf(id);
        
        // Xóa CV
        generatedCvManagementService.deleteCV(id);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("message", "Xóa CV thành công");
        
        return ResponseEntity.ok(response);
    }

}
