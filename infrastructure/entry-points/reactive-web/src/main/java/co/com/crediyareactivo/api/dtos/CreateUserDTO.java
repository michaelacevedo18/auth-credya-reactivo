package co.com.crediyareactivo.api.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreateUserDTO(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        String lastname,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate birthdate, // O usa LocalD

        @NotBlank(message = "La direccion es obligatoria")
        String address,

        @NotBlank(message = "El número de teléfono es obligatorio")
        String phoneNumber,

        @Email(message = "Debe ser un correo válido")
        @NotBlank(message = "El correo es obligatorio")
        String email,

        @NotBlank(message = "El password es obligatorio")
        String password,

        @NotNull(message = "El salario base es obligatorio")
        @DecimalMin(value = "0.0", inclusive = false, message = "El salario debe ser mayor que 0")
        @DecimalMax(value = "15000000.0", message = "El salario no debe superar 15 millones")
        Double baseSalary,

        @NotNull(message = "El numero de documento es obligatorio")
        Long idNumber,

        @NotNull(message = "El rol es obligatorio")
        Long rolId

) {}