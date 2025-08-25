package co.com.crediyareactivo.r2dbc.repositoriesimpl;
import co.com.crediyareactivo.model.user.models.User;

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
public class UserRepositoryAdapter implements co.com.crediyareactivo.model.user.gateways.UserRepository {

    private final UserRepository repo;
    private final ObjectMapper mapper;
    private final TransactionalUtils tx;

    @Override
    public Mono<User> save(User user) {
        return tx.execute(
                Mono.fromSupplier(() -> {
                            var entity = mapper.map(user, UserEntity.class);
                            entity.setId(null);               // ← asegura INSERT
                            return entity;
                        })
                        .flatMap(repo::save)
                        .map(saved -> mapper.map(saved, User.class))
        );
    }



    @Override
    public Mono<User> findById(String id) {
        return repo.findById(id)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repo.findByEmail(email)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Flux<User> findAll() {
        return repo.findAll()
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repo.deleteById(id);
    }

    @Override
    public Mono<User> findByRolId(Long rolId) {
        return repo.findByRolId(rolId)
                .map(entity -> mapper.map(entity, User.class));
    }

}