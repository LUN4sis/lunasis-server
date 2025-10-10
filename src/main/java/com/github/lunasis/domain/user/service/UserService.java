package com.github.lunasis.domain.user.service;

import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import com.github.lunasis.domain.user.dto.response.SimpleUserInfo;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public SimpleUserInfo updateUserInfo(User user, UpdateUserInfo updateUserInfo) {

        user.update(updateUserInfo);
        userRepository.save(user);
        return SimpleUserInfo.builder()
                .nickname(user.getNickname())
                .privateChat(user.getPrivateChat())
                .build();
    }


}
