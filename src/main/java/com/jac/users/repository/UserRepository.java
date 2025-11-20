package com.jac.users.repository;

import com.jac.entities.security.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {
    @Query("{ 'username': ?0 }")
    Mono<UserEntity> findByUsername(String username);

    Mono<Boolean> existsByUsername(String username);
}
