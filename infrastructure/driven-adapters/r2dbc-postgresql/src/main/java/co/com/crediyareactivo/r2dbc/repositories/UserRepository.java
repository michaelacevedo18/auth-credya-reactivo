package co.com.crediyareactivo.r2dbc.repositories;

import co.com.crediyareactivo.model.user.models.UserDomain;
import co.com.crediyareactivo.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface UserRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<Object> {
    Mono<UserEntity> findByEmail(String email);
    Mono<UserDomain> findByRolId(Long rolId);
}
