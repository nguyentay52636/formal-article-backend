package org.example.dto.request.ai;

import lombok.Data;

@Data
public class AiCvGenerateRequest {
    private String name;
    private String position;
    private String experience;
    private String projects;
}
