package com.github.lunasis.domain.product.service;

import com.github.lunasis.domain.product.dto.response.ProductListResponse;
import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductListResponse> getProductList(ProductCategory category) {

        return productRepository.findAllByCategory(category).stream().map(ProductListResponse::from).toList();

    }
}
