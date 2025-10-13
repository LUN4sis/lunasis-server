package com.github.lunasis.domain.chat.controller;

import com.github.lunasis.domain.chat.dto.request.QuestionRequest;
import com.github.lunasis.domain.chat.dto.response.ChatHistoryResponse;
import com.github.lunasis.domain.chat.dto.response.ChatListResponse;
import com.github.lunasis.domain.chat.dto.response.ChatResponse;
import com.github.lunasis.domain.chat.dto.response.StartChatResponse;
import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.chat.service.ChatService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @Operation(summary = "채팅 시작 api")
    public ApiResponse<StartChatResponse> startChat(@AuthenticationPrincipal User user, @Valid @RequestBody QuestionRequest questionRequest) {

        return ApiResponse.ok(chatService.startChat(user, questionRequest));
    }

    @PostMapping("/{chatRoom}")
    @Operation(summary = "채팅 보내기 api")
    public ApiResponse<ChatResponse> chat(@AuthenticationPrincipal User user, @PathVariable ChatRoom chatRoom,
                                          @Valid @RequestBody QuestionRequest questionRequest) {

        return ApiResponse.ok(chatService.chat(user, chatRoom, questionRequest));
    }

    @GetMapping
    @Operation(summary = "채팅방 목록 불러오기 api")
    public ApiResponse<List<ChatListResponse>> getChatRooms(@AuthenticationPrincipal User user) {

        return ApiResponse.ok(chatService.getChatRooms(user));
    }

    @GetMapping("/{chatRoom}")
    @Operation(summary = "채팅방 대화 내용 불러오기")
    public ApiResponse<List<ChatHistoryResponse>> getChatHistory(@AuthenticationPrincipal User user, @PathVariable ChatRoom chatRoom) {

        return ApiResponse.ok(chatService.getChatHistory(user, chatRoom));
    }
}
