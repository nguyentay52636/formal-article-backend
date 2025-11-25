package org.example.dto.request.template;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TemplateUpdateRequest {
    private String name;
    private String slug;
    private String summary;
    private String html;
    private String css;
    private String previewUrl;
    private String color;
    
    @Schema(description = "Mô tả chi tiết template")
    private String description;
    
    @Schema(description = "Ngôn ngữ sử dụng")
    private String language;
    
    @Schema(description = "Lĩnh vực sử dụng")
    private String usage;
    
    @Schema(description = "Phong cách thiết kế")
    private String design;
    
    @Schema(description = "Danh sách các tính năng nổi bật")
    private List<String> features;
    
    private Long tagId;
}