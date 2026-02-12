package com.github.lunasis.domain.chat.service;

import com.github.lunasis.domain.chat.dto.request.GuestQuestionRequest;
import com.github.lunasis.domain.chat.dto.request.LlmChatRequest;
import com.github.lunasis.domain.chat.dto.request.LlmGuestChatRequest;
import com.github.lunasis.domain.chat.dto.request.QuestionRequest;
import com.github.lunasis.domain.chat.dto.response.ChatHistoryResponse;
import com.github.lunasis.domain.chat.dto.response.ChatListResponse;
import com.github.lunasis.domain.chat.dto.response.ChatResponse;
import com.github.lunasis.domain.chat.dto.response.LlmChatResponse;
import com.github.lunasis.domain.chat.dto.response.StartChatResponse;
import com.github.lunasis.domain.chat.entity.Chat;
import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.chat.exception.ChatsExceptions;
import com.github.lunasis.domain.chat.repository.ChatRoomRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.domain.user.service.SavedMemoryService;
import com.github.lunasis.global.exception.ApiException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatService {

    @Value("${fastapi.url}")
    private String fastapiUrl;
    private final WebClient webClient;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SavedMemoryService savedMemoryService;

    @Transactional
    public StartChatResponse startChat(UUID userId, QuestionRequest questionRequest) {

        User user = userRepository.findById(userId).orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user(user)
                .privateChat(user.getPrivateChat())
                .build());

        List<String> savedMemorySummaries = savedMemoryService.getSavedMemorySummaries(userId);

        LlmChatResponse response = sendChat(
                LlmChatRequest.of(user, chatRoom, questionRequest.question(), savedMemorySummaries));
        chatRoom.updateTitle(response.title());

        saveChatToRoom(chatRoom, questionRequest.question(), response);

        return StartChatResponse.builder()
                .chatRoomId(chatRoom.getId())
                .title(response.title())
                .answer(response.answer())
                .build();
    }


    @Transactional
    public ChatResponse chat(User user, UUID chatRoomId, QuestionRequest questionRequest) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatsExceptions.CHATROOM_NOT_FOUND::toException);

        if (!hasChatRoomAccess(user, chatRoom)) {
            throw ChatsExceptions.USER_NOT_ALLOWED.toException();
        }

        List<String> savedMemorySummaries = savedMemoryService.getSavedMemorySummaries(user.getId());

        LlmChatResponse response = sendChat(
                LlmChatRequest.of(user, chatRoom, questionRequest.question(), savedMemorySummaries));

        saveChatToRoom(chatRoom, questionRequest.question(), response);

        return ChatResponse.builder()
                .answer(response.answer())
                .build();
    }

    private LlmChatResponse sendChat(LlmChatRequest llmChatRequest) {
        try {
            return webClient.post()
                    .uri(fastapiUrl + "/ai/chat")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(llmChatRequest)
                    .retrieve()
                    .bodyToMono(LlmChatResponse.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage(), 500);
        }
    }

    private void saveChatToRoom(ChatRoom chatRoom, String question, LlmChatResponse response) {
        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .question(question)
                .answer(response.answer())
                .build();

        chatRoom.getChats().add(chat);
        chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void updateChatRoomTitle(User user, UUID chatRoomId, String title) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatsExceptions.CHATROOM_NOT_FOUND::toException);

        if (!hasChatRoomAccess(user, chatRoom)) {
            throw ChatsExceptions.USER_NOT_ALLOWED.toException();
        }

        chatRoom.updateTitle(title);
        chatRoomRepository.save(chatRoom);

    }

    private boolean hasChatRoomAccess(User user, ChatRoom chatRoom) {

        return user.getId().equals(chatRoom.getUser().getId());
    }

    public ChatResponse anonymousChat(GuestQuestionRequest request) {

        return anonymousLlmChat(request);
    }

    private ChatResponse anonymousLlmChat(GuestQuestionRequest request) {
        try {
            return webClient.post()
                    .uri(fastapiUrl + "/api/chat/{chatRoomId}/anonymous", request.anonymousId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(LlmGuestChatRequest.from(request))
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage(), 500);
        }
    }

    public List<ChatListResponse> getChatRooms(User user) {

        return user.getChatRooms().stream().map(ChatListResponse::from).toList();
    }

    public List<ChatHistoryResponse> getChatHistory(User user, UUID chatRoomId) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatsExceptions.CHATROOM_NOT_FOUND::toException);

        if (!user.getId().equals(chatRoom.getUser().getId())) {
            throw ChatsExceptions.USER_NOT_ALLOWED.toException();
        }

        return chatRoom.getChats().stream().map(ChatHistoryResponse::from).toList();
    }


}
