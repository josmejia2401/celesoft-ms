package com.celesoft.conductors.repository;

import com.celesoft.entities.notifications.ApiKeyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ApiKeyRepository extends R2dbcRepository<ApiKeyEntity, Long> {
    @Query(value = "SELECT u FROM celesoft.api_keys WHERE user_id = :userId")
    Flux<ApiKeyEntity> findByUserId(Long userId);
}
