package com.github.lunasis.domain.product.service;

import com.github.lunasis.domain.product.dto.response.ProductListResponse;
import com.github.lunasis.domain.product.dto.response.ProductPriceListResponse;
import com.github.lunasis.domain.product.dto.response.SimplePriceResponse;
import com.github.lunasis.domain.product.entity.Bundle;
import com.github.lunasis.domain.product.entity.Product;
import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.product.entity.ProductUrl;
import com.github.lunasis.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductListResponse> getProductList(ProductCategory category) {

        return productRepository.findAllByCategory(category).stream().map(ProductListResponse::from).toList();

    }

    public ProductPriceListResponse getProductPriceList(Product product) {

        List<SimplePriceResponse> prices = product.getBundles().stream()
                .map(bundle -> SimplePriceResponse.builder()
                        .id(bundle.getId())
                        .count(bundle.getCount())
                        .price(getLowestPrice(bundle))
                        .mallCount(bundle.getProductUrls().size())
                        .build())
                .toList();

        return ProductPriceListResponse.builder()
                .name(product.getName())
                .prices(prices)
                .build();
    }


    private BigDecimal getLowestPrice(Bundle bundle) {
        return bundle.getProductUrls().stream()
                .map(ProductUrl::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
