package com.celesoft.auth.repository;

import com.celesoft.entities.security.UserEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserAuthRepository extends ReactiveCrudRepository<UserEntity, Long> {
    @Query("SELECT * FROM users WHERE username = :username")
    Mono<UserEntity> findByUsername(String username);
}
