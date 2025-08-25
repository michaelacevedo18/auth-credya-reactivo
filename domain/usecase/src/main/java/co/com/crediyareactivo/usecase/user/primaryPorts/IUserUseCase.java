package co.com.crediyareactivo.usecase.user.primaryPorts;

import co.com.crediyareactivo.model.user.models.UserDomain;
import reactor.core.publisher.Mono;

public interface IUserUseCase {
    Mono<UserDomain> apply(UserDomain user);
}
