package org.example.dto.request.favouriteCv;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavouriteCvUpdateRequest {
    @NotNull(message = "User ID không được để trống")
    private Long userId;
    
    @NotNull(message = "Template ID không được để trống")
    private Long templateId;
}
