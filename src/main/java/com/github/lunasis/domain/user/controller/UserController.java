package com.github.lunasis.domain.user.controller;

import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import com.github.lunasis.domain.user.dto.response.SimpleUserInfo;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.service.UserService;
import com.github.lunasis.global.dto.ApiResponse;
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
    public ApiResponse<SimpleUserInfo> updateUser(@AuthenticationPrincipal User user, @RequestBody UpdateUserInfo updateUserInfo) {

        return ApiResponse.ok(userService.updateUserInfo(user, updateUserInfo));
    }

}
