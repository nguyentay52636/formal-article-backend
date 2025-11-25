package org.example.exceptions;

import org.example.service.GeneratedCvService.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ==================== JSON Parse Errors ====================

    /**
     * Xử lý lỗi JSON parse (request body không hợp lệ)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", "error");
        
        String message = ex.getMessage();
        if (message != null && message.contains("JSON parse error")) {
            error.put("message", "JSON không hợp lệ. Vui lòng kiểm tra lại request body.");
            
            // Trích xuất chi tiết lỗi
            if (message.contains("Illegal unquoted character") || message.contains("CTRL-CHAR")) {
                error.put("detail", "Phát hiện ký tự xuống dòng hoặc tab trong JSON string. Hãy đảm bảo prompt nằm trên một dòng hoặc escape các ký tự đặc biệt (\\n, \\t).");
                error.put("example", "{\"prompt\": \"Tạo CV cho vị trí Developer với 3 năm kinh nghiệm\"}");
            } else if (message.contains("Unexpected character")) {
                error.put("detail", "Ký tự không mong đợi trong JSON. Kiểm tra cú pháp JSON.");
            } else if (message.contains("Unexpected end")) {
                error.put("detail", "JSON bị thiếu ký tự đóng. Kiểm tra dấu ngoặc {} hoặc \".");
            }
        } else {
            error.put("message", "Request body không đọc được. Vui lòng kiểm tra định dạng JSON.");
        }
        
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    // ==================== CV Service Exceptions ====================
    
    @ExceptionHandler(CvNotFoundException.class)
    public ResponseEntity<?> handleCvNotFoundException(CvNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(TemplateNotFoundException.class)
    public ResponseEntity<?> handleTemplateNotFoundException(TemplateNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(CvGenerationException.class)
    public ResponseEntity<?> handleCvGenerationException(CvGenerationException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // ==================== Validation Errors ====================

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorMessage.append(fieldError.getDefaultMessage()).append("; ");
        });
        return buildErrorResponse(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    // ==================== Database Errors ====================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String message = ex.getMessage();
        String errorMsg;
        
        if (message != null && message.toLowerCase().contains("email")) {
            errorMsg = "Email đã được sử dụng";
        } else if (message != null && message.toLowerCase().contains("phone")) {
            errorMsg = "Số điện thoại đã được sử dụng";
        } else if (message != null && message.toLowerCase().contains("constraint")) {
            errorMsg = "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại thông tin";
        } else {
            errorMsg = "Dữ liệu không hợp lệ";
        }
        
        return buildErrorResponse(errorMsg, HttpStatus.BAD_REQUEST);
    }

    // ==================== Generic Errors ====================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex, WebRequest request) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        return buildErrorResponse("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    // ==================== Helper Methods ====================
    
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("status", "error");
        error.put("message", message);
        return new ResponseEntity<>(error, status);
    }
}
