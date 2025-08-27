package co.com.crediyareactivo.model.user.gateways;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import reactor.core.publisher.Mono;

public interface AuthRepositoryGateway {
    Mono<UserResponseDomain> authenticateEmailPwd(String email, String pwd);
}
