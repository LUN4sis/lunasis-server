package com.github.lunasis.domain.user.controller;

import com.github.lunasis.domain.user.dto.request.CheckNickname;
import com.github.lunasis.domain.user.dto.request.UpdateChatSetting;
import com.github.lunasis.domain.user.dto.request.UpdatePreference;
import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import com.github.lunasis.domain.user.dto.response.SimpleUserInfo;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.UserService;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "사용자 정보 수정")
    public ApiResponse<SimpleUserInfo> updateUser(@AuthenticationPrincipal User user,
                                                  @RequestBody UpdateUserInfo updateUserInfo) {

        return ApiResponse.ok(userService.updateUserInfo(user, updateUserInfo));
    }

    @PostMapping("/preference")
    @Operation(summary = "사용자 선호도 수정")
    public ApiResponse<Void> updatePreference(@AuthenticationPrincipal User user,
                                              @RequestBody @Valid UpdatePreference updatePreference) {

        userService.updatePreference(user, updatePreference);
        return ApiResponse.ok();
    }

    @PostMapping("/check")
    @Operation(summary = "닉네임 중복 확인")
    public ApiResponse<Void> checkNickname(@Valid @RequestBody CheckNickname checkNickname) {

        if (!userService.checkNickname(checkNickname.nickname())) {
            return ApiResponse.ok();
        } else {
            return ApiResponse.error("중복된 닉네임이 있습니다", 404);
        }
    }

    @PostMapping("/setting")
    @Operation(summary = "채팅 개인 설정")
    public ApiResponse<Void> updateChatSetting(@AuthenticationPrincipal User user,
                                               @Valid @RequestBody UpdateChatSetting updateChatSetting) {

        userService.updateChatSetting(user.getId(), updateChatSetting);
        return ApiResponse.ok();
    }

}
