package com.github.lunasis.domain.chat.service;

import com.github.lunasis.domain.chat.dto.request.GuestQuestionRequest;
import com.github.lunasis.domain.chat.dto.request.LlmChatRequest;
import com.github.lunasis.domain.chat.dto.request.LlmGuestChatRequest;
import com.github.lunasis.domain.chat.dto.request.QuestionRequest;
import com.github.lunasis.domain.chat.dto.response.ChatHistoryResponse;
import com.github.lunasis.domain.chat.dto.response.ChatListResponse;
import com.github.lunasis.domain.chat.dto.response.ChatResponse;
import com.github.lunasis.domain.chat.dto.response.LlmStartChatResponse;
import com.github.lunasis.domain.chat.dto.response.StartChatResponse;
import com.github.lunasis.domain.chat.entity.Chat;
import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.chat.exception.ChatsExceptions;
import com.github.lunasis.domain.chat.repository.ChatRoomRepository;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.repository.UserRepository;
import com.github.lunasis.global.exception.ApiException;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
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

    @Transactional
    public StartChatResponse startChat(UUID userId, QuestionRequest questionRequest) {

        User user = userRepository.findById(userId).orElseThrow(UserExceptions.USER_NOT_FOUND::toException);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user(user)
                .privateChat(user.getPrivateChat())
                .build());

        LlmStartChatResponse response = sendFirstChat(
                LlmChatRequest.of(user, chatRoom.getId(), questionRequest.question()));
        chatRoom.updateTitle(response.title());

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .question(questionRequest.question())
                .answer(response.answer())
                .questionEmbedding(response.embedding())
                .build();

        chatRoom.getChats().add(chat);
        chatRoomRepository.save(chatRoom);

        return StartChatResponse.builder()
                .chatRoomId(chatRoom.getId())
                .title(response.title())
                .answer(response.answer())
                .build();
    }

    private LlmStartChatResponse sendFirstChat(LlmChatRequest llmChatRequest) {
        try {
            return webClient.post()
                    .uri(fastapiUrl + "/api/chat/{chatRoomId}/start", llmChatRequest.chatRoomId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(llmChatRequest)
                    .retrieve()
                    .bodyToMono(LlmStartChatResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException(e.getMessage(), 500);
        }
    }

    @Transactional
    public ChatResponse chat(User user, UUID chatRoomId, QuestionRequest questionRequest) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(ChatsExceptions.CHATROOM_NOT_FOUND::toException);

        if (!hasChatRoomAccess(user, chatRoom)) {
            throw ChatsExceptions.USER_NOT_ALLOWED.toException();
        }

        //Todo: llm에 질문 전달

        String answer = "test answer";

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .question(questionRequest.question())
                .answer(answer)
                .build();

        chatRoom.getChats().add(chat);
        chatRoomRepository.save(chatRoom);

        return ChatResponse.builder()
                .answer(answer)
                .build();
    }

    @Transactional
    public void updateTitle(User user, UUID chatRoomId, String title) {

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
