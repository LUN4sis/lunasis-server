package com.github.lunasis.domain.chat.dto.request;

import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.user.entity.ChatSetting;
import com.github.lunasis.domain.user.entity.ChatSetting.Level;
import com.github.lunasis.domain.user.entity.User;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
public record LlmChatRequest(
        UUID userId,
        UUID chatRoomId,
        String question,
        String sessionSummary,
        List<String> savedMemory,
        Boolean isFull,
        PreferenceRequest preference
) {
    public record PreferenceRequest(
            Level warmth,
            Level enthusiastic,
            Level formal,
            Set<ProductCategory> productCategories,
            String personalSetting,
            String userName,
            Integer age
    ) {
    }


    public static LlmChatRequest of(User user, ChatRoom chatRoom, String question, List<String> savedMemory) {

        ChatSetting chatSetting = user.getChatSetting();

        return LlmChatRequest.builder()
                .userId(user.getId())
                .chatRoomId(chatRoom.getId())
                .question(question)
                .sessionSummary(chatRoom.getSessionMemory())
                .savedMemory(savedMemory)
                .isFull(savedMemory.size() >= 10)
                .preference(new PreferenceRequest(
                        chatSetting.getWarmth(),
                        chatSetting.getEnthusiastic(),
                        chatSetting.getFormal(),
                        user.getPreference().getProductCategories(),
                        chatSetting.getPersonalSetting(),
                        user.getNickname(),
                        user.getAge()
                )).build();
    }
}
