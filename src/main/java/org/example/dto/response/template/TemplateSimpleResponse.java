package org.example.dto.response.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateSimpleResponse {
    private Long id;
    private String name;
    private String slug;
    private String summary;
    private String previewUrl;
    private String color;
    private String description;
    private String language;
    private String usage;
    private String design;
    private Long views;
    private Long downloads;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
