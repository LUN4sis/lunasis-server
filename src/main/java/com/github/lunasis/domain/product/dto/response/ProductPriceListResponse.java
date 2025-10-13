package com.github.lunasis.domain.product.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductPriceListResponse(
        String name,
        List<SimplePriceResponse> prices
) {
}
