package co.com.crediyareactivo.usecase.user;


import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.gateways.ports.JWTServicePort;
import co.com.crediyareactivo.model.user.gateways.ports.PasswordEncoderPort;
import co.com.crediyareactivo.model.user.models.RolEnum;
import co.com.crediyareactivo.model.user.models.UserDomain;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepositoryGateway userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private JWTServicePort jwtService;

    private AuthenticateUserUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new AuthenticateUserUseCase(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void shouldErrorWhenUserNotFound() {
        String email = "nouser@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.authenticateEmailPwd(email, "pwd"))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Usuario no encontrado"))
                .verify();

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void shouldErrorWhenPasswordDoesNotMatch() {
        String email = "user@example.com";
        String rawPwd = "wrong";
        UserDomain user = UserDomain.builder()
                .email(email)
                .password("ENC(correct)")
                .rolId(1L)
                .idNumber(123456L)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(rawPwd, "ENC(correct)")).thenReturn(false);

        StepVerifier.create(useCase.authenticateEmailPwd(email, rawPwd))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Contrasenia incorrecta"))
                .verify();

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPwd, "ENC(correct)");
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldReturnResponseWhenCredentialsAreValid() {
        String email = "user@example.com";
        String rawPwd = "secret";
        Long rolId = 1L;
        String roleName = RolEnum.getNameById(rolId); // usamos la misma lógica de producción
        UserDomain user = UserDomain.builder()
                .email(email)
                .password("ENC(secret)")
                .rolId(rolId)
                .idNumber(987654L)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(rawPwd, "ENC(secret)")).thenReturn(true);
        when(jwtService.generateToken(email, roleName, "987654")).thenReturn("jwt-token-123");

        StepVerifier.create(useCase.authenticateEmailPwd(email, rawPwd))
                .assertNext(resp -> {
                    assertThat(resp).isInstanceOf(UserResponseDomain.class);
                    assertThat(resp.getEmail()).isEqualTo(email);
                    assertThat(resp.getRolName()).isEqualTo(roleName);
                    assertThat(resp.getIdNumber()).isEqualTo(987654L);
                    assertThat(resp.getToken()).isEqualTo("jwt-token-123");
                })
                .verifyComplete();

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPwd, "ENC(secret)");
        verify(jwtService).generateToken(email, roleName, "987654");
    }

    @Test
    void shouldPropagateErrorWhenJwtGenerationFails() {
        String email = "user@example.com";
        String rawPwd = "secret";
        Long rolId = 2L;
        String roleName = RolEnum.getNameById(rolId);
        UserDomain user = UserDomain.builder()
                .email(email)
                .password("ENC(secret)")
                .rolId(rolId)
                .idNumber(112233L)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(passwordEncoder.matches(rawPwd, "ENC(secret)")).thenReturn(true);
        when(jwtService.generateToken(email, roleName, "112233"))
                .thenThrow(new RuntimeException("JWT service unavailable"));

        StepVerifier.create(useCase.authenticateEmailPwd(email, rawPwd))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("JWT service unavailable"))
                .verify();

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(rawPwd, "ENC(secret)");
        verify(jwtService).generateToken(email, roleName, "112233");
    }
}
