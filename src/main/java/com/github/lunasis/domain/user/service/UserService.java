package com.github.lunasis.domain.user.service;

import com.github.lunasis.domain.user.dto.request.UpdateChatSetting;
import com.github.lunasis.domain.user.dto.request.UpdatePreference;
import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import com.github.lunasis.domain.user.dto.response.NicknameResponse;
import com.github.lunasis.domain.user.dto.response.SimpleUserInfo;
import com.github.lunasis.domain.user.entity.User;
import com.github.lunasis.domain.user.exception.UserExceptions;
import com.github.lunasis.domain.user.module.RandomNicknameGenerator;
import com.github.lunasis.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserService {

    private final UserRepository userRepository;
    private final RandomNicknameGenerator randomNicknameGenerator;

    @Transactional
    public SimpleUserInfo updateUserInfo(User user, UpdateUserInfo updateUserInfo) {

        user.update(updateUserInfo);
        userRepository.save(user);
        return SimpleUserInfo.builder()
                .nickname(user.getNickname())
                .build();
    }


    @Transactional
    public void updatePreference(User user, UpdatePreference updatePreference) {

        User currentUser = getUserById(user.getId());

        currentUser.updateFirstLogin();
        currentUser.getPreference().updateUserPreference(updatePreference);
        userRepository.save(currentUser);
    }

    @Transactional
    public void updateChatSetting(UUID userId, UpdateChatSetting updateChatSetting) {

        User user = getUserById(userId);

        user.getChatSetting().update(updateChatSetting);
        userRepository.save(user);
    }

    public NicknameResponse getRandomNickname(UUID userID) {

        User user = getUserById(userID);
        String nickname = randomNicknameGenerator.generate();

        user.updateNickName(nickname);
        userRepository.save(user);
        return NicknameResponse.builder()
                .randomNickname(nickname)
                .build();
    }

    public User getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserExceptions.USER_NOT_FOUND::toException);
    }


}
