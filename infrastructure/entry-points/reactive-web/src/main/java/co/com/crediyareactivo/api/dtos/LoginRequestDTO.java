package co.com.crediyareactivo.api.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record LoginRequestDTO(

        @Email(message = "Debe ser un correo valido")
        @NotBlank(message = "El correo es obligatorio")
        String email,

        @NotBlank(message = "El password es obligatorio")
        String password

) {}