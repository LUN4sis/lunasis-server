package com.github.lunasis.domain.user.service;

import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.chat.exception.ChatsExceptions;
import com.github.lunasis.domain.chat.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void saveMemories(SaveMemory memory) {

        saveSavedMemory(memory.userId(), memory.savedMemory());
        saveSessionMemory(memory.chatRoomId(), memory.sessionMemory(), memory.embedding());
    }

    private void saveSavedMemory(UUID userId, String savedMemory) {

        if (savedMemory == null) {
            return;
        }

        Integer savedMemoryCount = savedMemoryRepository.countByUserId(userId);
        if (savedMemoryCount >= 10) {
            return;
        }

        SavedMemory newSavedMemory = SavedMemory.builder()
                .userId(userId)
                .summary(savedMemory)
                .build();

        savedMemoryRepository.save(newSavedMemory);

    }

    private void saveSessionMemory(UUID charRoomId, String sessionMemory, float[] embedding) {

        if (sessionMemory == null) {
            return;
        }

        ChatRoom chatRoom = chatRoomRepository.findById(charRoomId)
                .orElseThrow(ChatsExceptions.CHATROOM_NOT_FOUND::toException);
        chatRoom.saveSession(sessionMemory, embedding);
        chatRoomRepository.save(chatRoom);
    }

    public List<SavedMemoryResponse> getSavedMemories(UUID userId) {

        return savedMemoryRepository.findAllByUserId(userId).stream()
                .map(SavedMemoryResponse::from).toList();
    }

    public List<String> getSavedMemorySummaries(UUID userId) {

        return savedMemoryRepository.findAllByUserId(userId).stream()
                .map(SavedMemory::getSummary)
                .toList();
    }

    @Transactional
    public void deleteSavedMemory(UUID savedMemoryId) {

        savedMemoryRepository.deleteById(savedMemoryId);
    }
}
