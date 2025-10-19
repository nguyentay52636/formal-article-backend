package org.example.service;

import org.example.dto.VaiTroDto.VaiTroRequestDTO;
import org.example.dto.VaiTroDto.VaiTroResponseDTO;
import org.example.entity.VaiTro;
import org.example.mapping.VaiTroMapper;
import org.example.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class VaiTroService {

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private VaiTroMapper vaiTroMapper;

    private static final Set<String> ALLOWED_TEN = new HashSet<>(Arrays.asList(
            "doc_gia", "tac_gia", "quan_tri", "bien_tap"
    ));

    // Create
    public VaiTroResponseDTO create(VaiTroRequestDTO request) {
        if (request.getTen() == null || !ALLOWED_TEN.contains(request.getTen())) {
            throw new RuntimeException("Invalid 'ten'. Allowed: doc_gia, tac_gia, quan_tri, bien_tap");
        }
        
        // Check if ma already exists
        if (vaiTroRepository.findByMa(request.getMa()).isPresent()) {
            throw new RuntimeException("Mã vai trò đã tồn tại: " + request.getMa());
        }
        
        VaiTro vaiTro = new VaiTro();
        vaiTro.setMa(request.getMa());
        vaiTro.setTen(request.getTen());
        
        VaiTro saved = vaiTroRepository.save(vaiTro);
        return vaiTroMapper.toResponseDTO(saved);
    }

    // Read all
    public List<VaiTroResponseDTO> findAll() {
        return vaiTroRepository.findAll().stream()
                .map(vaiTroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Read by id
    public VaiTroResponseDTO findById(Long id) {
        VaiTro vaiTro = vaiTroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò với id: " + id));
        return vaiTroMapper.toResponseDTO(vaiTro);
    }

    // Read by code
    public VaiTroResponseDTO findByMa(String ma) {
        VaiTro vaiTro = vaiTroRepository.findByMa(ma)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò với mã: " + ma));
        return vaiTroMapper.toResponseDTO(vaiTro);
    }

    // Update
    public VaiTroResponseDTO update(Long id, VaiTroRequestDTO request) {
        VaiTro existing = vaiTroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò với id: " + id));

        if (request.getMa() != null) {
            // Check if new ma already exists (excluding current record)
            if (!existing.getMa().equals(request.getMa()) && 
                vaiTroRepository.findByMa(request.getMa()).isPresent()) {
                throw new RuntimeException("Mã vai trò đã tồn tại: " + request.getMa());
            }
            existing.setMa(request.getMa());
        }
        
        if (request.getTen() != null) {
            if (!ALLOWED_TEN.contains(request.getTen())) {
                throw new RuntimeException("Invalid 'ten'. Allowed: doc_gia, tac_gia, quan_tri, bien_tap");
            }
            existing.setTen(request.getTen());
        }

        VaiTro saved = vaiTroRepository.save(existing);
        return vaiTroMapper.toResponseDTO(saved);
    }

    // Delete
    public void delete(Long id) {
        if (!vaiTroRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy vai trò với id: " + id);
        }
        vaiTroRepository.deleteById(id);
    }

    // Initialize default roles if missing
    public List<VaiTroResponseDTO> initDefaultRoles() {
        return Arrays.asList("doc_gia", "tac_gia", "quan_tri", "bien_tap").stream()
                .map(code -> {
                    VaiTro vaiTro = vaiTroRepository.findByMa(code)
                            .orElseGet(() -> vaiTroRepository.save(new VaiTro(null, code, code)));
                    return vaiTroMapper.toResponseDTO(vaiTro);
                })
                .collect(Collectors.toList());
    }
}


