package org.example.dto.response.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.tag.TagResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateResponse {
    private Long id;
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
    
    private Long views;
    private Long downloads;
    private TagResponse tag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
