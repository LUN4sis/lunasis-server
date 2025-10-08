package com.github.lunasis.domain.auth.repository;

import com.github.lunasis.domain.auth.entity.ExchangeToken;
import org.springframework.data.repository.CrudRepository;

public interface ExchangeTokenRepository extends CrudRepository<ExchangeToken, String> {
}
