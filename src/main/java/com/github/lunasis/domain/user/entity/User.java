package com.github.lunasis.domain.user.entity;

import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.post.entity.Post;
import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(unique = true)
    private String nickname;

    @Column(name = "google_id", nullable = false, unique = true)
    private String googleId;

    @Column(name = "age")
    private Integer age;

    @Column(name = "profile")
    private String profile;

    @Column(name = "private_chat")
    @Builder.Default
    private Boolean privateChat = false;

    @ElementCollection(targetClass = Insurance.class)
    @CollectionTable(
            name = "user_insurances",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "insurances")
    @Builder.Default
    private Set<Insurance> insurance = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ChatRoom> chatRooms;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Preference preference;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;


    public void update(UpdateUserInfo updateUserInfo) {

        this.nickname = updateUserInfo.nickname();
        this.age = updateUserInfo.age();
        this.insurance = updateUserInfo.insurance();
        this.privateChat = updateUserInfo.privateChat();

    }

}
