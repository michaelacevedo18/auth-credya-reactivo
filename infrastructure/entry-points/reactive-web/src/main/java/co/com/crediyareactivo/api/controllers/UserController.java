package co.com.crediyareactivo.api.controllers;
import co.com.crediyareactivo.api.dtos.CreateUserDTO;
import co.com.crediyareactivo.api.dtos.ResponseDTO;
import co.com.crediyareactivo.api.helpers.PasswordHashService;
import co.com.crediyareactivo.api.mapper.UserMapper;
import co.com.crediyareactivo.model.user.models.UserDomain;
import co.com.crediyareactivo.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private final UserUseCase useCase;
    private final PasswordHashService passwordHashService;

    @PostMapping
    public Mono<ResponseEntity<ResponseDTO<UserDomain>>> create(@Valid @RequestBody Mono<CreateUserDTO> dtoMono) {
        return dtoMono
                .map(UserMapper::toDomain)
                .map(user -> {
                    user.setPassword(passwordHashService.encode(user.getPassword()));
                    return user;
                })
                .flatMap(useCase::apply)
                .map(user -> ResponseEntity.ok(
                        ResponseDTO.<UserDomain>builder()
                                .success(true)
                                .message("Usuario registrado exitosamente")
                                .data(user)
                                .statusCode(200)
                                .timestamp(LocalDateTime.now())
                                .build()
                ));
    }

}
