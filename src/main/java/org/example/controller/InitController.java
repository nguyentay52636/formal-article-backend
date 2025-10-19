package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.VaiTro;
import org.example.repository.VaiTroRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/init")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Initialization", description = "Initialize sample data")
public class InitController {

    private final VaiTroRepository vaiTroRepository;

    @PostMapping("/roles")
    @Operation(summary = "Initialize roles", description = "Create default roles if they don't exist")
    public ResponseEntity<?> initRoles() {
        try {
            // Check if roles already exist
            if (vaiTroRepository.count() > 0) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Roles already exist");
                return ResponseEntity.ok(response);
            }

            // Create default roles
            VaiTro adminRole = new VaiTro();
            adminRole.setMa("quan_tri");
            adminRole.setTen("Quản trị viên");
            adminRole.setTrangThai("ACTIVE");
            adminRole.setNgayTao(LocalDateTime.now());
            vaiTroRepository.save(adminRole);

            VaiTro editorRole = new VaiTro();
            editorRole.setMa("bien_tap");
            editorRole.setTen("Biên tập viên");
            editorRole.setTrangThai("ACTIVE");
            editorRole.setNgayTao(LocalDateTime.now());
            vaiTroRepository.save(editorRole);

            VaiTro authorRole = new VaiTro();
            authorRole.setMa("tac_gia");
            authorRole.setTen("Tác giả");
            authorRole.setTrangThai("ACTIVE");
            authorRole.setNgayTao(LocalDateTime.now());
            vaiTroRepository.save(authorRole);

            VaiTro readerRole = new VaiTro();
            readerRole.setMa("doc_gia");
            readerRole.setTen("Độc giả");
            readerRole.setTrangThai("ACTIVE");
            readerRole.setNgayTao(LocalDateTime.now());
            vaiTroRepository.save(readerRole);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Roles initialized successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Failed to initialize roles", e);
            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to initialize roles");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
