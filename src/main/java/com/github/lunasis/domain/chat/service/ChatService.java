package com.github.lunasis.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.github.lunasis.domain.user.service.SavedMemoryService;
import com.github.lunasis.domain.user.service.UserService;
import com.github.lunasis.global.exception.ApiException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    @Value("${fastapi.url}")
    private String fastapiUrl;
    private final WebClient webClient;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final SavedMemoryService savedMemoryService;

    @Transactional
    public StartChatResponse startChat(UUID userId, QuestionRequest questionRequest) throws JsonProcessingException {

        User user = userService.getUserById(userId);

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user(user)
                .privateChat(false)
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
    public ChatResponse chat(UUID userId, UUID chatRoomId, QuestionRequest questionRequest)
            throws JsonProcessingException {

        User user = userService.getUserById(userId);

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

    private LlmChatResponse sendChat(LlmChatRequest llmChatRequest) throws JsonProcessingException {
        String requestUrl = fastapiUrl + "/ai/chat";
        log.info("[FastAPI 요청 시작] URL: {}", requestUrl);
        log.debug("[FastAPI 요청 데이터] userId: {}, chatRoomId: {}, questionLength: {}",
                llmChatRequest.userId(), llmChatRequest.chatRoomId(),
                llmChatRequest.question() != null ? llmChatRequest.question().length() : 0);
        ObjectMapper mapper = new ObjectMapper();
        log.info("요청 JSON: {}", mapper.writeValueAsString(llmChatRequest));

        try {
            LlmChatResponse response = webClient.post()
                    .uri(requestUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(llmChatRequest)
                    .retrieve()
                    .bodyToMono(LlmChatResponse.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            log.info("[FastAPI 응답 성공] URL: {}, hasTitle: {}, answerLength: {}",
                    requestUrl, response != null && response.title() != null,
                    response != null && response.answer() != null ? response.answer().length() : 0);
            return response;

        } catch (Exception e) {
            log.error("[FastAPI 요청 실패] URL: {}, 에러 타입: {}, 에러 메시지: {}",
                    requestUrl, e.getClass().getName(), e.getMessage());
            log.error("[FastAPI 요청 실패] 상세 스택 트레이스: ", e);
            throw new ApiException("FastAPI 채팅 요청 실패: " + e.getMessage(), 500);
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
        String requestUrl = fastapiUrl + "/api/chat/" + request.anonymousId() + "/anonymous";
        log.info("[FastAPI 익명 채팅 요청 시작] URL: {}, anonymousId: {}", requestUrl, request.anonymousId());
        log.debug("[FastAPI 익명 채팅 요청 데이터] questionLength: {}",
                request.question() != null ? request.question().length() : 0);

        try {
            ChatResponse response = webClient.post()
                    .uri(fastapiUrl + "/api/chat/{chatRoomId}/anonymous", request.anonymousId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(LlmGuestChatRequest.from(request))
                    .retrieve()
                    .bodyToMono(ChatResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            log.info("[FastAPI 익명 채팅 응답 성공] URL: {}, answerLength: {}",
                    requestUrl, response != null && response.answer() != null ? response.answer().length() : 0);
            return response;

        } catch (Exception e) {
            log.error("[FastAPI 익명 채팅 요청 실패] URL: {}, anonymousId: {}, 에러 타입: {}, 에러 메시지: {}",
                    requestUrl, request.anonymousId(), e.getClass().getName(), e.getMessage());
            log.error("[FastAPI 익명 채팅 요청 실패] 상세 스택 트레이스: ", e);
            throw new ApiException("FastAPI 익명 채팅 요청 실패: " + e.getMessage(), 500);
        }
    }

    public List<ChatListResponse> getChatRooms(UUID userId) {

        User user = userService.getUserById(userId);
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
