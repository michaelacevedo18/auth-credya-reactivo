package co.com.crediyareactivo.api.controllers;

import co.com.crediyareactivo.api.dtos.LoginRequestDTO;
import co.com.crediyareactivo.api.dtos.ResponseDTO;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import co.com.crediyareactivo.usecase.user.AuthenticateUserUseCase;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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

    private final AuthenticateUserUseCase authRepository;

    @PostMapping("/login")
    public Mono<ResponseEntity<ResponseDTO<UserResponseDomain>>> login(@Valid @RequestBody Mono<LoginRequestDTO> loginDTOMono) {
        return loginDTOMono
                .flatMap(dto -> authRepository.authenticateEmailPwd(dto.email(), dto.password()))
                .map(user -> ResponseEntity.ok(
                        ResponseDTO.<UserResponseDomain>builder()
                                .success(true)
                                .message("Inicio de sesion exitoso")
                                .data(user)
                                .statusCode(200)
                                .timestamp(LocalDateTime.now())
                                .build()
                ));
    }

    @GetMapping("/customer")
    public Mono<ResponseEntity<String>> customerEndpoint() {
        return Mono.just(ResponseEntity.ok("Acceso autorizado solo para rol 2"));
    }



}
