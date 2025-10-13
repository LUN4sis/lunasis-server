package com.github.lunasis.domain.product.controller;

import com.github.lunasis.domain.product.dto.response.ProductListResponse;
import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.product.service.ProductService;
import com.github.lunasis.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<ProductListResponse>> getProducts(@RequestParam("category") ProductCategory category) {

        return ApiResponse.ok(productService.getProductList(category));
    }
}
