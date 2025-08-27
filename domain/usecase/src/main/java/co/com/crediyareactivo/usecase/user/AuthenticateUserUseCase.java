package co.com.crediyareactivo.usecase.user;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.models.UserDomain;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import co.com.crediyareactivo.usecase.user.primaryPorts.IAuthenticateUserUseCase;
import co.com.crediyareactivo.usecase.user.primaryPorts.IUserUseCase;
import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthenticateUserUseCase implements IAuthenticateUserUseCase {
    private final UserRepositoryGateway userRepository;

    //private final JwtService jwtService;

 /**   @Override
    public Mono<UserResponseDomain> authenticateEmailPwd(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Usuario no encontrado")))
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        String token = jwtService.generateToken(user.getEmail(), user.getRolId());
                        return Mono.just(new UserResponseDomain(user.getEmail(), user.getRolId(), token));
                    } else {
                        return Mono.error(new IllegalArgumentException("Contraseña incorrecta"));
                    }
                });
    }**/
}
