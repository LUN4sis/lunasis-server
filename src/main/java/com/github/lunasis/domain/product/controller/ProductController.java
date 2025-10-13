package com.github.lunasis.domain.product.controller;

import com.github.lunasis.domain.product.dto.response.ProductListResponse;
import com.github.lunasis.domain.product.dto.response.ProductPriceListResponse;
import com.github.lunasis.domain.product.entity.Product;
import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.product.service.ProductService;
import com.github.lunasis.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{product}")
    public ApiResponse<ProductPriceListResponse> getProductPrice(@PathVariable Product product) {

        return ApiResponse.ok(productService.getProductPriceList(product));
    }
}
