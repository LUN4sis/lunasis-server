package com.github.lunasis.domain.user.entity;

import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.user.dto.request.UpdatePreference;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
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
@Table(name = "preferences")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 선호조사 용

    // 헬스케어 관심 항목
    @ElementCollection(targetClass = HealthCareInterest.class)
    @CollectionTable(
            name = "user_healthcare_interests",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "healthcare_interest")
    @Builder.Default
    private Set<HealthCareInterest> healthCareInterests = new HashSet<>();

    //여성의학 관심 분야
    @ElementCollection(targetClass = GynecologyInterest.class)
    @CollectionTable(
            name = "gynecology_interest",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "gynecology_interest")
    @Builder.Default
    private Set<GynecologyInterest> gynecologyInterests = new HashSet<>();

    //여성 병원 방문 여부
    @Column(name = "has_visited")
    private Boolean hasVisited;

    // 여성 병원 선택 시 중요 요소
    @ElementCollection(targetClass = HospitalPriority.class)
    @CollectionTable(
            name = "user_hospital_priorities",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "hospital_priority")
    @Builder.Default
    private Set<HospitalPriority> hospitalPriorities = new HashSet<>();

    // 커뮤니티 관심 항목
    @ElementCollection(targetClass = CommunityInterest.class)
    @CollectionTable(
            name = "user_community_interests",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "community_interest")
    @Builder.Default
    private Set<CommunityInterest> communityInterests = new HashSet<>();

    // 커머스 관심 항목
    @ElementCollection(targetClass = CommerceInterest.class)
    @CollectionTable(
            name = "user_commerce_interests",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "commerce_interest")
    @Builder.Default
    private Set<CommerceInterest> commerceInterests = new HashSet<>();

    // 여성 용품 사용
    @ElementCollection(targetClass = ProductCategory.class)
    @CollectionTable(
            name = "user_product_categories",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    @Builder.Default
    private Set<ProductCategory> productCategories = new HashSet<>();


    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateUserPreference(UpdatePreference updatePreference) {
        this.healthCareInterests = updatePreference.healthCareInterests();
        this.gynecologyInterests = updatePreference.gynecologyInterests();
        this.hasVisited = updatePreference.hasVisited();
        this.hospitalPriorities = updatePreference.hospitalPriorities();
        this.communityInterests = updatePreference.communityInterests();
        this.commerceInterests = updatePreference.commerceInterests();
        this.productCategories = updatePreference.productCategories();
    }

    public void assignUser(User user) {
        this.user = user;
    }
}
