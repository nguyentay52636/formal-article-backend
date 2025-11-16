package org.example.dto.response.favouriteCv;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.response.template.TemplateResponse;
import org.example.dto.response.user.UserResponse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteCvResponse {
    private Long id;
    private Long userId;
    private UserResponse user;
    private Long templateId;
    private TemplateResponse template;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
