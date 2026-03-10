package com.github.lunasis.domain.user.dto.request;

import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.user.entity.CommerceInterest;
import com.github.lunasis.domain.user.entity.CommunityInterest;
import com.github.lunasis.domain.user.entity.GynecologyInterest;
import com.github.lunasis.domain.user.entity.HealthCareInterest;
import com.github.lunasis.domain.user.entity.HospitalPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record UpdatePreference(

        @Schema(description = "헬스케어 관심 항목")
        Set<HealthCareInterest> healthCareInterests,

        @Schema(description = "여성의학 관심 분야")
        Set<GynecologyInterest> gynecologyInterests,

        @Schema(description = "여성 병원 방문 여부")
        Boolean hasVisited,

        @Schema(description = "여성 병원 선택 시 중요 요소")
        Set<HospitalPriority> hospitalPriorities,

        @Schema(description = "커뮤니티 관심 항목")
        Set<CommunityInterest> communityInterests,

        @Schema(description = "커머스 관심 항목")
        Set<CommerceInterest> commerceInterests,

        @Schema(description = "사용자가 주로 사용하는 상품 카테고리 목록")
        Set<ProductCategory> productCategories

) {
}
