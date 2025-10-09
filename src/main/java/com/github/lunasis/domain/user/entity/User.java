package com.github.lunasis.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "profile")
    private String profile;

    @Column(name = "private_chat")
    @Builder.Default
    private boolean privateChat = false;


}
