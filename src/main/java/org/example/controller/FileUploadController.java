package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@Tag(name = "File Upload", description = "API quản lý file upload")
@RequiredArgsConstructor
public class FileUploadController {
        private static final String UPLOAD_DIR = "uploads/images/";

    @Autowired
    private CloudinaryService cloudinaryService;

    @Operation(summary = "Upload file", description = "Upload file lên Cloudinary")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload thành công"),
            @ApiResponse(responseCode = "400", description = "Lỗi upload")
    })
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file, "uploads");
            return ResponseEntity.ok(Map.of("url", url));
        } catch (Exception e) {
            e.printStackTrace(); 
            String errorMessage = e.getMessage();
            if (e.getCause() != null) {
                errorMessage += " - Cause: " + e.getCause().getMessage();
            }
            return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
        }
    }
    @Operation(summary = "Upload image to local", description = "Upload hình ảnh vào thư mục local")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload thành công"),
            @ApiResponse(responseCode = "500", description = "Lỗi server")
    })
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            java.io.File uploadDir = new java.io.File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.nio.file.Path filePath = java.nio.file.Paths.get(UPLOAD_DIR + fileName);

            java.nio.file.Files.write(filePath, file.getBytes());

            String fileUrl = "http://localhost:8000/" + UPLOAD_DIR + fileName;

            return ResponseEntity.ok().body(Map.of(
                    "message", "Upload thành công",
                    "fileName", fileName,
                    "url", fileUrl
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi upload: " + e.getMessage());
        }
    }
}
