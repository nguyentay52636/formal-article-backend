package org.example.dto.response.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    @Schema(description = "ID của tag", example = "1")
    private Long id;
    
    @Schema(description = "Tên của tag", example = "Công nghệ thông tin")
    private String name;
    
    @Schema(description = "Slug của tag", example = "cong-nghe-thong-tin")
    private String slug;

    @Schema(description = "Loại tag", example = "job_field")
    private String type;
}

