package com.github.lunasis.domain.product.controller;

import com.github.lunasis.domain.product.dto.response.MallResponse;
import com.github.lunasis.domain.product.dto.response.ProductListResponse;
import com.github.lunasis.domain.product.dto.response.ProductPriceListResponse;
import com.github.lunasis.domain.product.entity.Bundle;
import com.github.lunasis.domain.product.entity.Product;
import com.github.lunasis.domain.product.entity.ProductCategory;
import com.github.lunasis.domain.product.service.ProductService;
import com.github.lunasis.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "상품 목록 불러오기 api")
    public ApiResponse<List<ProductListResponse>> getProducts(@RequestParam("category") ProductCategory category) {

        return ApiResponse.ok(productService.getProductList(category));
    }

    @GetMapping("/{product}")
    @Operation(summary = "상품별 가격리스트 불어오기 api")
    public ApiResponse<ProductPriceListResponse> getProductPrice(@PathVariable Product product) {

        return ApiResponse.ok(productService.getProductPriceList(product));
    }

    @GetMapping("/{bundle}")
    @Operation(summary = "해당 수량(번들) 별 쇼핑몰 리스트 불러오기 api")
    public ApiResponse<List<MallResponse>> getBundleMallList(@PathVariable Bundle bundle) {

        return ApiResponse.ok(productService.getMallPrice(bundle));
    }
}
