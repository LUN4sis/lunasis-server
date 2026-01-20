package com.github.lunasis.domain.user.service;

import com.github.lunasis.domain.user.dto.request.SaveMemory;
import com.github.lunasis.domain.user.dto.response.SavedMemoryResponse;
import com.github.lunasis.domain.user.entity.SavedMemory;
import com.github.lunasis.domain.user.repository.SavedMemoryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SavedMemoryService {
    private final SavedMemoryRepository savedMemoryRepository;

    @Transactional
    public void saveSummaryMemory(SaveMemory memory) {

        Integer count = savedMemoryRepository.countByUserId(memory.userId());

        if (count >= 10) {
            return;
        }

        SavedMemory newSavedMemory = SavedMemory.builder()
                .userId(memory.userId())
                .summary(memory.savedMemory())
                .embedding(memory.embedding())
                .build();

        savedMemoryRepository.save(newSavedMemory);
    }

    public List<SavedMemoryResponse> getSavedMemories(UUID userId) {

        return savedMemoryRepository.findAllByUserId(userId).stream()
                .map(SavedMemoryResponse::from).toList();
    }

    @Transactional
    public void deleteSavedMemory(UUID savedMemoryId) {

        savedMemoryRepository.deleteById(savedMemoryId);
    }
}
