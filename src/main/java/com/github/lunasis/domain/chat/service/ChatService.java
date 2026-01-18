package com.github.lunasis.domain.chat.service;

import com.github.lunasis.domain.chat.dto.request.QuestionRequest;
import com.github.lunasis.domain.chat.dto.response.ChatHistoryResponse;
import com.github.lunasis.domain.chat.dto.response.ChatListResponse;
import com.github.lunasis.domain.chat.dto.response.ChatResponse;
import com.github.lunasis.domain.chat.dto.response.StartChatResponse;
import com.github.lunasis.domain.chat.entity.Chat;
import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.chat.exception.ChatsExceptions;
import com.github.lunasis.domain.chat.repository.ChatRoomRepository;
import com.github.lunasis.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatService {

    @Value("${fastapi.url}")
    private String fastapiUrl;
    private WebClient webClient;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public StartChatResponse startChat(User user, QuestionRequest questionRequest) {

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .user(user)
                .privateChat(user.getPrivateChat())
                .build());

        //TODO: llm에 전달 해주기
        String answer = "test answer";
        String title = "test title";
        chatRoom.updateTitle(title);

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .question(questionRequest.question())
                .answer(answer)
                .build();

        chatRoom.getChats().add(chat);
        chatRoomRepository.save(chatRoom);

        return StartChatResponse.builder()
                .chatRoomId(chatRoom.getId())
                .title(title)
                .answer(answer)
                .build();
    }

//    private LlmChatResponse SendFirstChat(UUID chatRoomId) {
//        webClient.post()
//                .uri(fastapiUrl+"/api/chat/{chatRoomId}",chatRoomId)
//                .contentType(MediaType.APPLICATION_JSON);
//    }

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

    public ChatResponse anonymousChat(QuestionRequest questionRequest) {

        //TODO: llm에 질문 전달

        String answer = "test answer";

        return ChatResponse.builder()
                .answer(answer)
                .build();
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
