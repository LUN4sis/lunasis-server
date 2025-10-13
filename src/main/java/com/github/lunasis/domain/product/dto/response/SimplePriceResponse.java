package com.github.lunasis.domain.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record SimplePriceResponse(
        UUID id,
        Integer count,
        BigDecimal price,
        Integer mallCount

) {
}
