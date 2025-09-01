package co.com.crediyareactivo.usecase.user;

import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.gateways.ports.PasswordEncoderPort;
import co.com.crediyareactivo.model.user.models.UserDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepositoryGateway userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Captor
    private ArgumentCaptor<UserDomain> userCaptor;

    private UserUseCase userUseCase;

    @BeforeEach
    void setup() {
        userUseCase = new UserUseCase(userRepository, passwordEncoder);
    }

    @Test
    void shouldThrowIfEmailAlreadyExists_andNotEncodeOrSave() {
        UserDomain existing = UserDomain.builder()
                .email("admin@example.com")
                .build();

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Mono.just(existing));

        StepVerifier.create(userUseCase.apply(existing))
                .expectErrorMatches(e -> e instanceof IllegalStateException &&
                        e.getMessage().equals("Email ya registrado"))
                .verify();

        verify(userRepository, times(1)).findByEmail("admin@example.com");
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldThrowIfAdminAlreadyExists_butPasswordIsEncoded_beforeCheck() {
        UserDomain newAdmin = UserDomain.builder()
                .email("newadmin@example.com")
                .password("plain")
                .rolId(1L)
                .build();

        when(userRepository.findByEmail("newadmin@example.com"))
                .thenReturn(Mono.empty());
        when(passwordEncoder.encode("plain"))
                .thenReturn("ENC(plain)");
        // ya existe admin -> error
        when(userRepository.findByRolId(1L))
                .thenReturn(Mono.just(UserDomain.builder().email("exists@example.com").build()));

        StepVerifier.create(userUseCase.apply(newAdmin))
                .expectErrorSatisfies(error -> {
                    assertThat(error).isInstanceOf(IllegalStateException.class);
                    assertThat(error.getMessage()).isEqualTo("Ya existe un administrador registrado");
                })
                .verify();

        // Se consultó email, se codificó password, se consultó admin, pero NO se guardó
        verify(userRepository).findByEmail("newadmin@example.com");
        verify(passwordEncoder).encode("plain");
        verify(userRepository).findByRolId(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldSaveAdminIfNoAdminExists_passwordEncoded() {
        UserDomain newAdmin = UserDomain.builder()
                .email("newadmin@example.com")
                .password("plain")
                .rolId(1L)
                .build();

        when(userRepository.findByEmail("newadmin@example.com"))
                .thenReturn(Mono.empty());
        when(passwordEncoder.encode("plain"))
                .thenReturn("ENC(plain)");
        when(userRepository.findByRolId(1L))
                .thenReturn(Mono.empty());
        // Nota: el use case muta el mismo objeto "input" y lo pasa a save
        when(userRepository.save(any(UserDomain.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.apply(newAdmin))
                .assertNext(saved -> {
                    assertThat(saved.getEmail()).isEqualTo("newadmin@example.com");
                    assertThat(saved.getRolId()).isEqualTo(1L);
                    assertThat(saved.getPassword()).isEqualTo("ENC(plain)");
                })
                .verifyComplete();

        verify(userRepository).findByEmail("newadmin@example.com");
        verify(passwordEncoder).encode("plain");
        verify(userRepository).findByRolId(1L);
        verify(userRepository).save(userCaptor.capture());

        UserDomain captured = userCaptor.getValue();
        assertThat(captured.getPassword()).isEqualTo("ENC(plain)");
    }

    @Test
    void shouldSaveNonAdmin_passwordEncoded_whenRolIsNotAdmin() {
        UserDomain user = UserDomain.builder()
                .email("user@example.com")
                .password("mypwd")
                .rolId(2L) // no admin
                .build();

        when(userRepository.findByEmail("user@example.com")).thenReturn(Mono.empty());
        when(passwordEncoder.encode("mypwd")).thenReturn("ENC(mypwd)");
        when(userRepository.save(any(UserDomain.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.apply(user))
                .assertNext(saved -> {
                    assertThat(saved.getEmail()).isEqualTo("user@example.com");
                    assertThat(saved.getRolId()).isEqualTo(2L);
                    assertThat(saved.getPassword()).isEqualTo("ENC(mypwd)");
                })
                .verifyComplete();

        verify(userRepository).findByEmail("user@example.com");
        verify(passwordEncoder).encode("mypwd");
        verify(userRepository).save(userCaptor.capture());

        UserDomain captured = userCaptor.getValue();
        assertThat(captured.getPassword()).isEqualTo("ENC(mypwd)");
    }

    @Test
    void shouldSaveWhenRolIsNull_treatedAsNonAdmin_andEncodePassword() {
        UserDomain user = UserDomain.builder()
                .email("nouserrole@example.com")
                .password("abc123")
                .rolId(null) // null => rama no admin
                .build();

        when(userRepository.findByEmail("nouserrole@example.com")).thenReturn(Mono.empty());
        when(passwordEncoder.encode("abc123")).thenReturn("ENC(abc123)");
        when(userRepository.save(any(UserDomain.class)))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(userUseCase.apply(user))
                .expectNextMatches(saved ->
                        "nouserrole@example.com".equals(saved.getEmail()) &&
                                "ENC(abc123)".equals(saved.getPassword()) &&
                                saved.getRolId() == null)
                .verifyComplete();

        verify(userRepository).findByEmail("nouserrole@example.com");
        verify(passwordEncoder).encode("abc123");
        verify(userRepository).save(any(UserDomain.class));
    }
}
