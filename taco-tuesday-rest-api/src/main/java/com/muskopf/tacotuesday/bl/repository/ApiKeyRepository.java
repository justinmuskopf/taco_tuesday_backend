package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {
    boolean existsByKey(String apiKey);
}
