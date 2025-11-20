package com.jac.auth.repository;

import com.jac.entities.security.TokenEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TokenAuthRepository extends ReactiveMongoRepository<TokenEntity, Long> {
    Mono<Void> deleteByUserIdAndAudienceAndAppName(Long userId, String audience, String appName);
}
