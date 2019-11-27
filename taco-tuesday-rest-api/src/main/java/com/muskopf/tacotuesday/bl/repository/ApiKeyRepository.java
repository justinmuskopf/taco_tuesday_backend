package com.muskopf.tacotuesday.bl.repository;

import com.muskopf.tacotuesday.domain.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ApiKeyRepository extends JpaRepository<ApiKey, Integer> {
    boolean existsByKey(String apiKey);
}
