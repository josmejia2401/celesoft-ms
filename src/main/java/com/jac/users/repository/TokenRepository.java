package com.jac.users.repository;

import com.jac.entities.security.TokenEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TokenRepository extends ReactiveMongoRepository<TokenEntity, Long> {
    Mono<Void> deleteByUserId(Long userId);
}
