package co.com.crediyareactivo.usecase.user;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.models.UserDomain;
import co.com.crediyareactivo.usecase.user.primaryPorts.IUserUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
public class UserUseCase implements IUserUseCase {
    private final UserRepositoryGateway gateway;
    public Mono<UserDomain> apply(UserDomain input) {
        return gateway.findByEmail(input.getEmail())
                .flatMap(existing -> Mono.<UserDomain>error(new IllegalStateException("Email ya registrado")))
                .switchIfEmpty(
                        Mono.defer(() -> {
                            if (input.getRolId() != null && input.getRolId() == 1L) {
                                return gateway.findByRolId(1L)
                                        .flatMap(admin -> Mono.<UserDomain>error(new IllegalStateException("Ya existe un administrador registrado")))
                                        .switchIfEmpty(Mono.defer(() -> gateway.save(input)));
                            } else {
                                return gateway.save(input);
                            }
                        })
                );
    }






}
