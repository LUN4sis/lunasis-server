package com.github.lunasis.domain.user.controller;

import com.github.lunasis.domain.user.dto.request.CheckNickname;
import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import com.github.lunasis.domain.user.dto.response.SimpleUserInfo;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.UserService;
import com.github.lunasis.global.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<SimpleUserInfo> updateUser(@AuthenticationPrincipal User user, @RequestBody UpdateUserInfo updateUserInfo) {

        return ApiResponse.ok(userService.updateUserInfo(user, updateUserInfo));
    }

    @GetMapping("/check")
    public ApiResponse<Void> checkNickname(@Valid @RequestBody CheckNickname checkNickname) {

        if (!userService.checkNickname(checkNickname.nickname())) {
            return ApiResponse.ok();
        } else return ApiResponse.error("중복된 닉네임이 있습니다", 404);
    }

}
