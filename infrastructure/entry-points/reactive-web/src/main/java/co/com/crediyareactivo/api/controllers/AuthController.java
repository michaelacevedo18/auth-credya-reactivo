package co.com.crediyareactivo.api.controllers;

import co.com.crediyareactivo.api.dtos.CreateUserDTO;
import co.com.crediyareactivo.api.dtos.LoginRequestDTO;
import co.com.crediyareactivo.api.dtos.ResponseDTO;
import co.com.crediyareactivo.api.helpers.JwtService;
import co.com.crediyareactivo.api.helpers.PasswordHashService;
import co.com.crediyareactivo.api.mapper.UserMapper;
import co.com.crediyareactivo.model.user.gateways.UserRepositoryGateway;
import co.com.crediyareactivo.model.user.models.UserDomain;
import co.com.crediyareactivo.usecase.user.AuthenticateUserUseCase;
import co.com.crediyareactivo.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Creado exitosamente",
                content = @Content(schema = @Schema(implementation = ResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de usuario no validos"),
        @ApiResponse(responseCode = "500", description = "Error interno")
})
@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AuthController {

    private final UserRepositoryGateway userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody LoginRequestDTO login) {
        return userRepository.findByEmail(login.email())
                .filter(user -> encoder.matches(login.password(), user.getPassword()))
                .map(user -> {
                    String token = jwtService.generateToken(user.getEmail(), user.getRolId());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}
