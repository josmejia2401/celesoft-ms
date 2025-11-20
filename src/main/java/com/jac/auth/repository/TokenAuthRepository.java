package com.jac.auth.repository;

import com.jac.entities.security.TokenEntity;
import com.jac.entities.security.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TokenAuthRepository extends ReactiveMongoRepository<TokenEntity, Long> {
    @Query("{ 'userId': ?0, 'audience': ?1, 'appName': ?2 }")
    Mono<Void> deleteByUserIdAndAudienceAndAppName(Long userId, String audience, String appName);
}
