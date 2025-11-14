package com.github.lunasis.domain.user.dto.request;

import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.user.entity.Community;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

public record UpdatePreference(

        @Schema(description = "사용자가 선호하는 커뮤니티 목록")
        Set<Community> communities,

        @Schema(description = "사용자가 선호하는 상품 카테고리 목록")
        Set<ProductCategory> categories,

        @Schema(description = "가격 비교 선호 여부")
        Boolean priceComparison

) {
}
