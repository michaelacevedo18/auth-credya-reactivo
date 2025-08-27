package co.com.crediyareactivo.config;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.gateways.ports.PasswordEncoderPort;
import co.com.crediyareactivo.r2dbc.adapters.JwtServiceAdapter;
import co.com.crediyareactivo.usecase.user.AuthenticateUserUseCase;
import co.com.crediyareactivo.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {


        @Bean
        public UserUseCase userUseCase(
                UserRepositoryGateway repository,
                PasswordEncoderPort passwordEncoderPort) {
                return new UserUseCase(repository, passwordEncoderPort);
        }
        // Aquí puedes agregar más use cases si tienes más
        // @Bean
        // public UpdateUserUseCase updateUserUseCase(UserRepository repo) { ... }
        @Bean
        public AuthenticateUserUseCase authenticateUserUseCase(
                UserRepositoryGateway gateway,
                PasswordEncoderPort passwordEncoderPort,
                JwtServiceAdapter jwtService
        ) {
                return new AuthenticateUserUseCase(gateway, passwordEncoderPort, jwtService);
        }
}
