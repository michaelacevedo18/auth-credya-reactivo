package co.com.crediyareactivo.usecase.user;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.models.UserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserUseCaseTest {
    @Mock
    private UserRepositoryGateway userRepository;

    private UserUseCase userUseCase;

    @BeforeEach
    void setup() {
        userUseCase = new UserUseCase(userRepository);
    }

    @Test
    void shouldThrowIfEmailAlreadyExists() {
        UserDomain user = UserDomain.builder().email("admin@example.com").build();
        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.apply(user))
                .expectErrorMatches(e -> e instanceof IllegalStateException &&
                        e.getMessage().equals("Email ya registrado"))
                .verify();
    }

    @Test
    void shouldThrowIfAdminAlreadyExists() {
        UserDomain user = UserDomain.builder()
                .email("newadmin@example.com")
                .rolId(1L)
                .build();

        when(userRepository.findByEmail("newadmin@example.com")).thenReturn(Mono.empty());
        when(userRepository.findByRolId(1L)).thenReturn(Mono.just(UserDomain.builder().build()));

        StepVerifier.create(userUseCase.apply(user))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalStateException.class);
                    assertThat(error.getMessage()).isEqualTo("Ya existe un administrador registrado");
                })
                .verify();
    }




    @Test
    void shouldSaveUserIfValid() {
        UserDomain user = UserDomain.builder().email("new@example.com").rolId(2L).build();
        when(userRepository.findByEmail("new@example.com")).thenReturn(Mono.empty());
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(userUseCase.apply(user))
                .expectNext(user)
                .verifyComplete();
    }
}
