package com.github.lunasis.domain.user.repository;

import com.github.lunasis.domain.user.entity.SavedMemory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedMemoryRepository extends JpaRepository<SavedMemory, UUID> {
    Integer countByUserId(UUID userId);

    List<SavedMemory> findAllByUserId(UUID userId);
}
