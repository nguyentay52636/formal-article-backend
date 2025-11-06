package org.example.controller;

import org.example.service.WordToHtmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/api/word")
@CrossOrigin(origins = "*") 
public class WordPreviewController {

    @Autowired
    private WordToHtmlService wordToHtmlService;

    @PostMapping(value = "/preview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> previewWordFile(@RequestParam("file") MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            String htmlContent = wordToHtmlService.convertWordToHtml(inputStream);
            return Map.of("html", htmlContent);
        }
    }

    @GetMapping("/preview/{filename}")
    public Map<String, Object> previewExistingFile(@PathVariable String filename) throws IOException {
        File file = new File("src/main/resources/docs/" + filename);
        if (!file.exists()) {
            return Map.of("error", "File không tồn tại");
        }

        try (InputStream inputStream = new FileInputStream(file)) {
            String htmlContent = wordToHtmlService.convertWordToHtml(inputStream);
            return Map.of("html", htmlContent);
        }
    }
}
