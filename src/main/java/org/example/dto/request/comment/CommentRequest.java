package org.example.dto.request.comment;

import lombok.Data;

@Data
public class CommentRequest {
    private String content;
    private Long userId;
}
