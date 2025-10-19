package org.example.mapping;

import org.example.dto.VaiTroDto.VaiTroResponseDTO;
import org.example.entity.VaiTro;
import org.springframework.stereotype.Component;

@Component
public class VaiTroMapper {

    public VaiTroResponseDTO toResponseDTO(VaiTro entity) {
        if (entity == null) return null;
        
        return new VaiTroResponseDTO(
            entity.getId(),
            entity.getMa(),
            entity.getTen()
        );
    }
}
