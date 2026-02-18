package com.github.lunasis.domain.chat.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.lunasis.domain.chat.dto.request.GuestQuestionRequest;
import com.github.lunasis.domain.chat.dto.request.QuestionRequest;
import com.github.lunasis.domain.chat.dto.request.UpdateTitleRequest;
import com.github.lunasis.domain.chat.dto.response.ChatHistoryResponse;
import com.github.lunasis.domain.chat.dto.response.ChatListResponse;
import com.github.lunasis.domain.chat.dto.response.ChatResponse;
import com.github.lunasis.domain.chat.dto.response.StartChatResponse;
import com.github.lunasis.domain.chat.service.ChatService;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "채팅 시작 api")
    public ApiResponse<StartChatResponse> startChat(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody QuestionRequest questionRequest)
            throws JsonProcessingException {

        return ApiResponse.ok(chatService.startChat(user.getId(), questionRequest));
    }

    @PostMapping("/{chatRoomId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "채팅 보내기 api")
    public ApiResponse<ChatResponse> chat(@AuthenticationPrincipal User user, @PathVariable UUID chatRoomId,
                                          @Valid @RequestBody QuestionRequest questionRequest)
            throws JsonProcessingException {

        return ApiResponse.ok(chatService.chat(user.getId(), chatRoomId, questionRequest));
    }

    @PostMapping("/anonymous")
    @Operation(summary = "익명/비 로그인자 채팅 보내기 api")
    public ApiResponse<ChatResponse> anonymousChar(@Valid @RequestBody GuestQuestionRequest request) {

        return ApiResponse.ok(chatService.anonymousChat(request));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "채팅방 목록 불러오기 api")
    public ApiResponse<List<ChatListResponse>> getChatRooms(@AuthenticationPrincipal User user) {

        return ApiResponse.ok(chatService.getChatRooms(user.getId()));
    }

    @GetMapping("/{chatRoomId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "채팅방 대화 내용 불러오기")
    public ApiResponse<List<ChatHistoryResponse>> getChatHistory(@AuthenticationPrincipal User user,
                                                                 @PathVariable UUID chatRoomId) {

        return ApiResponse.ok(chatService.getChatHistory(user, chatRoomId));
    }

    @PostMapping("/{chatRoomId}/title")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "채팅방 제목 수정")
    public void updateTitle(@AuthenticationPrincipal User user, @PathVariable UUID chatRoomId,
                            @RequestBody UpdateTitleRequest request) {

        chatService.updateChatRoomTitle(user, chatRoomId, request.title());
    }

}
