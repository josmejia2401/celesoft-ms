package com.celesoft.auth.repository;

import com.celesoft.entities.core.TokenEntity;
import com.celesoft.entities.core.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TokenAuthRepository extends ReactiveCrudRepository<TokenEntity, Long> {
    @Query(value = "SELECT u FROM celesoft.users WHERE user_id = :userId AND audience = :audience And app_name =: appName")
    Mono<UserEntity> deleteByUserIdAndAudienceAndAppName(Long userId, String audience, String appName);
}
