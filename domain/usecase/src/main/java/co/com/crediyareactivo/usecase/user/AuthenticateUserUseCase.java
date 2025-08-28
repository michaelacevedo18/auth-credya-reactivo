package co.com.crediyareactivo.usecase.user;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.gateways.ports.JWTServicePort;
import co.com.crediyareactivo.model.user.gateways.ports.PasswordEncoderPort;
import co.com.crediyareactivo.model.user.models.RolEnum;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import co.com.crediyareactivo.usecase.user.primaryPorts.IAuthenticateUserUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthenticateUserUseCase implements IAuthenticateUserUseCase {
    private final UserRepositoryGateway gateway;
    private final PasswordEncoderPort passwordEncoder;
    private final JWTServicePort jwtService;
    @Override
    public Mono<UserResponseDomain> authenticateEmailPwd(String email, String password) {
        return gateway.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new RuntimeException("Contrasenia incorrecta"));
                    }
String roleName = RolEnum.getNameById(user.getRolId());
                    String token = jwtService.generateToken(user.getEmail(), roleName, user.getIdNumber().toString());

                    UserResponseDomain response = UserResponseDomain.builder()
                            .email(user.getEmail())
                            .token(token)
                            .idNumber(user.getIdNumber())
                            .rolName(roleName)
                            .build();

                    return Mono.just(response);
                });
    }
}
