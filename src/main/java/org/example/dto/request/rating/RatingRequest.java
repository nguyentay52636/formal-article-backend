package org.example.dto.request.rating;

import lombok.Data;

@Data
public class RatingRequest {
    private Integer score;
    private Long userId;
    private Long templateId;
}
