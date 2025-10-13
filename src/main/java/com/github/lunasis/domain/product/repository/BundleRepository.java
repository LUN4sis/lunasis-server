package com.github.lunasis.domain.product.repository;

import com.github.lunasis.domain.product.entity.Bundle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BundleRepository extends JpaRepository<Bundle, UUID> {
}
