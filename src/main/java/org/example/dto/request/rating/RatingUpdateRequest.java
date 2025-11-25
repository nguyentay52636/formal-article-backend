package org.example.dto.request.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RatingUpdateRequest {
    @Schema(description = "ID của rating", example = "1")
    private Long id;
    
    @Schema(description = "Điểm đánh giá mới (1-5)", example = "4")
    private Integer score;
    
    @Schema(description = "ID của template được đánh giá", example = "1")
    private Long templateId;

    @Schema(description = "ID của user đánh giá", example = "1")
    private Long userId;
}