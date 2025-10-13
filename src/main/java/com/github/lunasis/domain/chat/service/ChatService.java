package com.github.lunasis.domain.chat.service;

import com.github.lunasis.domain.chat.dto.request.QuestionRequest;
import com.github.lunasis.domain.chat.dto.response.StartChatResponse;
import com.github.lunasis.domain.chat.entity.Chat;
import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.chat.repository.ChatRoomRepository;
import com.github.lunasis.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public StartChatResponse startChat(User user, QuestionRequest questionRequest) {

        ChatRoom chatRoom = ChatRoom.builder()
                .user(user)
                .privateChat(user.getPrivateChat())
                .build();

        //TODO: llm에 전달 해주기
        String answer = "test answer";

        Chat chat = Chat.builder()
                .chatRoom(chatRoom)
                .question(questionRequest.question())
                .answer(answer)
                .build();

        chatRoom.getChats().add(chat);
        chatRoomRepository.save(chatRoom);

        return StartChatResponse.builder()
                .chatRoomId(chatRoom.getId())
                .answer(answer)
                .build();
    }
}
