package com.github.lunasis.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_setting")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "warmth")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Level warmth = Level.DEFAULT;

    @Column(name = "enthusiastic")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Level enthusiastic = Level.DEFAULT;

    @Column(name = "formal")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Level formal = Level.DEFAULT;

    @Column(name = "personal_setting", length = 1000)
    private String personalSetting;

    public void update(Level warmth, Level enthusiastic, Level formal, String personalSetting) {
        this.warmth = warmth;
        this.enthusiastic = enthusiastic;
        this.formal = formal;
        this.personalSetting = personalSetting;
    }

    public enum Level {
        HIGH, DEFAULT, LESS
    }
}
