package co.com.crediyareactivo.r2dbc.repositoriesimpl;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.models.UserDomain;

import co.com.crediyareactivo.r2dbc.entities.UserEntity;
import co.com.crediyareactivo.r2dbc.helper.TransactionalUtils;
import co.com.crediyareactivo.r2dbc.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryGateway {

    private final UserRepository repo;
    private final ObjectMapper mapper;
    private final TransactionalUtils tx;

    @Override
    public Mono<UserDomain> save(UserDomain user) {
        return tx.execute(
                Mono.fromSupplier(() -> {
                            var entity = mapper.map(user, UserEntity.class);
                            entity.setId(null);               // ← asegura INSERT
                            return entity;
                        })
                        .flatMap(repo::save)
                        .map(saved -> mapper.map(saved, UserDomain.class))
        );
    }



    @Override
    public Mono<UserDomain> findById(String id) {
        return repo.findById(id)
                .map(entity -> mapper.map(entity, UserDomain.class));
    }

    @Override
    public Mono<UserDomain> findByEmail(String email) {
        return repo.findByEmail(email)
                .map(entity -> mapper.map(entity, UserDomain.class));
    }

    @Override
    public Flux<UserDomain> findAll() {
        return repo.findAll()
                .map(entity -> mapper.map(entity, UserDomain.class));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repo.deleteById(id);
    }

    @Override
    public Mono<UserDomain> findByRolId(Long rolId) {
        return repo.findByRolId(rolId)
                .map(entity -> mapper.map(entity, UserDomain.class));
    }

}