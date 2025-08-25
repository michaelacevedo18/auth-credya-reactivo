package co.com.crediyareactivo.model.user.gateways;


import co.com.crediyareactivo.model.user.models.UserDomain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryGateway {
    Mono<UserDomain> save(UserDomain user);
    Mono<UserDomain> findById(String id);
    Mono<UserDomain> findByEmail(String email);
    Flux<UserDomain> findAll();
    Mono<Void> deleteById(String id);
    Mono<UserDomain> findByRolId(Long rolId);
}
