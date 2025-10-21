package org.example.repository;

import org.example.entity.The;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheRepository extends JpaRepository<The, Long> {
}
