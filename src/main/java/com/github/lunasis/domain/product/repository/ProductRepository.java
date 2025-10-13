package com.github.lunasis.domain.product.repository;

import com.github.lunasis.domain.product.entity.Product;
import com.github.lunasis.domain.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByCategory(ProductCategory category);
}
