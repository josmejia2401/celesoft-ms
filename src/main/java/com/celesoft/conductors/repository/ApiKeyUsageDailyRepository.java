package com.celesoft.conductors.repository;

import com.celesoft.entities.notifications.ApiKeyUsageDailyEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface ApiKeyUsageDailyRepository extends R2dbcRepository<ApiKeyUsageDailyEntity, Long> {

    @Query(value = "SELECT u FROM celesoft.api_key_usage_daily WHERE api_key_id = :apiKeyId AND day = :day AND channel = :channel")
    Mono<ApiKeyUsageDailyEntity> findByApiKeyIdAndDayAndChannel(Long apiKeyId, LocalDate day, String channel);
}
