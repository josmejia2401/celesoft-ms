package com.jac.cameras.repository;


import com.jac.entities.camera.CameraEntity;
import com.jac.utils.enums.camera.CameraStatusEnum;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CameraRepository extends ReactiveMongoRepository<CameraEntity, Long> {

    Flux<CameraEntity> findByStatus(CameraStatusEnum status);
}
