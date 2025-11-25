package org.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class để parse và clean response từ AI
 */
@Slf4j
public final class AICvResponseParser {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private AICvResponseParser() {
        // Utility class
    }
    
    /**
     * Kết quả parse từ AI response
     */
    public record ParsedCvResult(
            String title,
            String dataJson,        // String để lưu DB
            String styleJson,       // String để lưu DB
            String htmlOutput,
            JsonNode dataJsonNode,  // JsonNode để trả API
            JsonNode styleJsonNode  // JsonNode để trả API
    ) {
        public boolean isValid() {
            return title != null && dataJsonNode != null && styleJsonNode != null;
        }
    }
    
    /**
     * Parse AI response thành ParsedCvResult
     * 
     * @param aiResponse Response raw từ AI
     * @return ParsedCvResult hoặc null nếu parse thất bại
     */
    public static ParsedCvResult parse(String aiResponse) {
        try {
            String cleanedJson = cleanResponse(aiResponse);
            JsonNode root = objectMapper.readTree(cleanedJson);
            
            String title = extractString(root, "title", "CV");
            JsonNode dataJsonNode = extractNode(root, "dataJson");
            JsonNode styleJsonNode = extractNode(root, "styleJson");
            String htmlOutput = extractString(root, "htmlOutput", "");
            
            String dataJson = objectMapper.writeValueAsString(dataJsonNode);
            String styleJson = objectMapper.writeValueAsString(styleJsonNode);
            
            return new ParsedCvResult(title, dataJson, styleJson, htmlOutput, dataJsonNode, styleJsonNode);
            
        } catch (Exception e) {
            log.error("Error parsing AI response: {}", e.getMessage());
            log.debug("Raw AI response: {}", aiResponse);
            return null;
        }
    }
    
    /**
     * Làm sạch response từ AI
     * - Loại bỏ markdown code blocks
     * - Tìm JSON object hợp lệ
     */
    public static String cleanResponse(String response) {
        if (response == null || response.isBlank()) {
            return "{}";
        }
        
        String cleaned = response.trim();
        
        // Loại bỏ markdown code blocks
        cleaned = removeMarkdownBlocks(cleaned);
        
        // Tìm JSON object
        cleaned = extractJsonObject(cleaned);
        
        return cleaned.trim();
    }
    
    /**
     * Loại bỏ markdown code blocks (```json ... ```)
     */
    private static String removeMarkdownBlocks(String input) {
        String result = input;
        
        if (result.contains("```json")) {
            int start = result.indexOf("```json") + 7;
            int end = result.indexOf("```", start);
            if (end > start) {
                result = result.substring(start, end);
            } else {
                result = result.substring(start);
            }
        } else if (result.contains("```")) {
            int start = result.indexOf("```") + 3;
            int end = result.indexOf("```", start);
            if (end > start) {
                result = result.substring(start, end);
            } else {
                result = result.substring(start);
            }
        }
        
        return result.trim();
    }
    
    /**
     * Tìm và trích xuất JSON object từ string
     */
    private static String extractJsonObject(String input) {
        int startIndex = input.indexOf("{");
        int endIndex = input.lastIndexOf("}");
        
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            return input.substring(startIndex, endIndex + 1);
        }
        
        return input;
    }
    
    /**
     * Trích xuất string value từ JsonNode
     */
    private static String extractString(JsonNode node, String field, String defaultValue) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field).asText();
        }
        return defaultValue;
    }
    
    /**
     * Trích xuất JsonNode con
     */
    private static JsonNode extractNode(JsonNode node, String field) {
        if (node.has(field) && !node.get(field).isNull()) {
            return node.get(field);
        }
        return objectMapper.createObjectNode();
    }
    
    /**
     * Tạo empty result khi có lỗi
     */
    public static ParsedCvResult emptyResult() {
        ObjectNode emptyNode = objectMapper.createObjectNode();
        return new ParsedCvResult("CV", "{}", "{}", "", emptyNode, emptyNode);
    }
}
