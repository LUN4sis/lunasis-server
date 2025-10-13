package com.github.lunasis.domain.product.dto.response;

import com.github.lunasis.domain.product.entity.ProductUrl;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record MallResponse(

        UUID id,
        String image,
        String url,
        BigDecimal price
) {

    public static MallResponse from(ProductUrl productUrl) {

        return MallResponse.builder()
                .id(productUrl.getId())
                .image(productUrl.getUrl())
                .price(productUrl.getPrice())
                .build();
    }
}
