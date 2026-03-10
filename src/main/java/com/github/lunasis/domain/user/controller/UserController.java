package com.github.lunasis.domain.user.controller;

import com.github.lunasis.domain.user.dto.request.SaveMemory;
import com.github.lunasis.domain.user.dto.request.UpdateChatSetting;
import com.github.lunasis.domain.user.dto.request.UpdatePreference;
import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import com.github.lunasis.domain.user.dto.response.NicknameResponse;
import com.github.lunasis.domain.user.dto.response.SavedMemoryResponse;
import com.github.lunasis.domain.user.dto.response.SimpleUserInfo;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.SavedMemoryService;
import com.github.lunasis.domain.user.service.UserService;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SavedMemoryService savedMemoryService;

    @PostMapping
    @Operation(summary = "사용자 정보 수정")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SimpleUserInfo> updateUser(@AuthenticationPrincipal User user,
                                                  @RequestBody UpdateUserInfo updateUserInfo) {

        return ApiResponse.ok(userService.updateUserInfo(user.getId(), updateUserInfo));
    }

    @PostMapping("/preference")
    @Operation(summary = "사용자 선호도 수정")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> updatePreference(@AuthenticationPrincipal User user,
                                              @RequestBody @Valid UpdatePreference updatePreference) {

        userService.updatePreference(user, updatePreference);
        return ApiResponse.ok();
    }


    @PostMapping("/setting")
    @Operation(summary = "채팅 개인 설정")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> updateChatSetting(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody UpdateChatSetting updateChatSetting) {

        userService.updateChatSetting(user.getId(), updateChatSetting);
        return ApiResponse.ok();
    }

    @PostMapping("/summary")
    @Operation(summary = "저장 메모리 저장")
    public ApiResponse<Void> saveSummaryMemory(@Valid @RequestBody SaveMemory saveMemory) {

        savedMemoryService.saveMemories(saveMemory);
        return ApiResponse.ok();
    }

    @GetMapping("/summary")
    @Operation(summary = "저장 메모리 조회")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<List<SavedMemoryResponse>> getSavedMemories(@AuthenticationPrincipal User user) {
        return ApiResponse.ok(savedMemoryService.getSavedMemories(user.getId()));
    }

    @DeleteMapping("/summary/{savedMemoryId}")
    @Operation(summary = "저장 메모리 삭제")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> deleteSavedMemory(@PathVariable UUID savedMemoryId) {

        savedMemoryService.deleteSavedMemory(savedMemoryId);
        return ApiResponse.ok();
    }

    @GetMapping("/recommend")
    @Operation(summary = "랜덤 닉네임 추천")
    public ApiResponse<NicknameResponse> getRandomNickname(@AuthenticationPrincipal User user) {

        return ApiResponse.ok(userService.getRandomNickname(user.getId()));
    }

}
