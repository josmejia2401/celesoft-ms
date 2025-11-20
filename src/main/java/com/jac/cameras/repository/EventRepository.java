package com.jac.cameras.repository;

import com.jac.entities.camera.EventEntity;
import com.jac.utils.enums.camera.EventTypeEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EventRepository extends ReactiveMongoRepository<EventEntity, Long> {

    Flux<EventEntity> findByCameraId(Long cameraId);

    Flux<EventEntity> findByType(EventTypeEnum type);
}
