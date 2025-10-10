package com.github.lunasis.domain.user.dto.request;

import com.github.lunasis.domain.user.entity.Community;
import com.github.lunasis.domain.user.entity.Insurance;
import com.github.lunasis.domain.user.entity.ProductCategory;

public record UpdateUserInfo(

        String nickname,
        Integer age,
        Insurance insurance,
        String zipCode,
        Community community,
        ProductCategory[] productCategories,
        boolean priceComparison,
        boolean privateChat

) {
}
