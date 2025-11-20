package com.celesoft.auth.repository;

import com.celesoft.entities.security.TokenEntity;
import com.celesoft.entities.security.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TokenAuthRepository extends ReactiveMongoRepository<TokenEntity, Long> {
    @Query(value = "SELECT u FROM users WHERE userId = :userId AND audience = :audience And appName =: appName")
    Mono<UserEntity> deleteByUserIdAndAudienceAndAppName(Long userId, String audience, String appName);
}
