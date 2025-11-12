package com.celesoft.conductors.repository;

import com.celesoft.entities.notifications.StatusEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface StatusRepository extends R2dbcRepository<StatusEntity, Long> {

    @Query(value = "SELECT u FROM celesoft.status WHERE category = :category AND code = :code")
    Mono<StatusEntity> findByCategoryAndCode(String category, String code);
}
