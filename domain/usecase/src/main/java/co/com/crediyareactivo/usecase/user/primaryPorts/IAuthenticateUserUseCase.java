package co.com.crediyareactivo.usecase.user.primaryPorts;

import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import reactor.core.publisher.Mono;

public interface IAuthenticateUserUseCase {
     Mono<UserResponseDomain> authenticateEmailPwd(String email, String pwd);
}
