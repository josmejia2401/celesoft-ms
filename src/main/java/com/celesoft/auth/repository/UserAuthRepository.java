package com.celesoft.auth.repository;

import com.celesoft.entities.core.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserAuthRepository extends ReactiveCrudRepository<UserEntity, Long> {
    @Query("SELECT * FROM celesoft.users WHERE username = :username")
    Mono<UserEntity> findByUsername(String username);
}
