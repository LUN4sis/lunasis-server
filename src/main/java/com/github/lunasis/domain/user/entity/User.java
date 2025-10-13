package com.github.lunasis.domain.user.entity;

import com.github.lunasis.domain.chat.entity.ChatRoom;
import com.github.lunasis.domain.user.dto.request.UpdateUserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Builder
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ChatRoom> chatRooms;
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
    @Enumerated(EnumType.STRING)
    @Column(name = "insurance")
    private Insurance insurance;
    @Column(name = "zip_code")
    private String zipCode;
    // 선호조사 용
    @Enumerated(EnumType.STRING)
    @Column(name = "community")
    private Community community;
    @ElementCollection(targetClass = ProductCategory.class)
    @CollectionTable(
            name = "user_product_categories",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    @Builder.Default
    private Set<ProductCategory> productCategory = new HashSet<>();
    //선호조사 용
    @Column(name = "price_comparison")
    @Builder.Default
    private Boolean priceComparison = false;

    public void update(UpdateUserInfo updateUserInfo) {

        this.nickname = updateUserInfo.nickname();
        this.age = updateUserInfo.age();
        this.insurance = updateUserInfo.insurance();
        this.zipCode = updateUserInfo.zipCode();
        this.community = updateUserInfo.community();
        this.privateChat = updateUserInfo.privateChat();
        this.priceComparison = updateUserInfo.priceComparison();

        if (updateUserInfo.productCategories() != null) {
            this.productCategory = new HashSet<>(Arrays.asList(updateUserInfo.productCategories()));
        } else {
            this.productCategory = new HashSet<>();
        }

    }

}
