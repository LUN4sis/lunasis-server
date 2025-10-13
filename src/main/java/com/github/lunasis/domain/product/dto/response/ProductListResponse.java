package com.github.lunasis.domain.product.dto.response;

import com.github.lunasis.domain.product.entity.Product;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ProductListResponse(

        UUID productId,
        String name,
        String image,
        String description,
        List<String> badges
) {
    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .image(product.getImage())
                .description(product.getDescription())
                .badges(product.getBadges())
                .build();
    }
}
