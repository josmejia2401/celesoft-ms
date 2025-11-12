package com.celesoft.conductors.repository;

import com.celesoft.entities.notifications.NotificationEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends R2dbcRepository<NotificationEntity, Long> {
    @Query(value = "SELECT u FROM celesoft.notifications WHERE api_key_id = :apiKeyId")
    Flux<NotificationEntity> findByApiKeyId(Long apiKeyId);
}
