package org.example.dto.request.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateRequest {
    @Schema(description = "Tên của tag", example = "Công nghệ thông tin")
    private String name;
    
    @Schema(description = "Slug của tag (URL friendly)", example = "cong-nghe-thong-tin")
    private String slug;

    @Schema(description = "Loại tag (job_field, position, design)", example = "job_field")
    private String type;
}
