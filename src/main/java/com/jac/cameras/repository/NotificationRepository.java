package com.jac.cameras.repository;


import com.jac.entities.camera.NotificationEntity;
import com.jac.utils.enums.camera.NotificationStatusEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface NotificationRepository extends ReactiveMongoRepository<NotificationEntity, Long> {

    Flux<NotificationEntity> findByUserId(Long userId);

    Flux<NotificationEntity> findByStatus(NotificationStatusEnum status);
}
