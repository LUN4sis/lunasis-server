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
    @ElementCollection(targetClass = Community.class)
    @CollectionTable(
            name = "user_communities",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "community")
    @Builder.Default
    private Set<Community> communities = new HashSet<>();

    @ElementCollection(targetClass = ProductCategory.class)
    @CollectionTable(
            name = "user_product_categories",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "product_category")
    @Builder.Default
    private Set<ProductCategory> productCategories = new HashSet<>();

    @Column(name = "price_comparison")
    @Builder.Default
    private Boolean priceComparison = false;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void updateUserPreference(UpdatePreference updatePreference) {
        this.communities = updatePreference.communities();
        this.productCategories = updatePreference.categories();
        this.priceComparison = updatePreference.priceComparison();
    }

    public void assignUser(User user) {
        this.user = user;
    }
}
