package co.com.crediyareactivo.config;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.usecase.user.UserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public UserUseCase userUseCase(UserRepositoryGateway repository) {
                return new UserUseCase(repository);
        }

        // Aquí puedes agregar más use cases si tienes más
        // @Bean
        // public UpdateUserUseCase updateUserUseCase(UserRepository repo) { ... }
}
