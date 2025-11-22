package org.example.dto.request.template;

import lombok.Data;

@Data
public class TemplateUpdateRequest {
    private String name;
    private String slug;
    private String summary;
    private String html;
    private String css;
    private String previewUrl;
    private Long tagId;
}