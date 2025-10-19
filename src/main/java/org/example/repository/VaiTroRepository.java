package org.example.repository;

import org.example.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Long> {
    Optional<VaiTro> findByMa(String ma);
}
