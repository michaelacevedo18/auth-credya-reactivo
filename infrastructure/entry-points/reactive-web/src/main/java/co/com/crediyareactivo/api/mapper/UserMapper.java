package co.com.crediyareactivo.api.mapper;

import co.com.crediyareactivo.api.dtos.CreateUserDTO;
import co.com.crediyareactivo.model.user.models.UserDomain;

public class UserMapper {
    public static UserDomain toDomain(CreateUserDTO dto) {
        return UserDomain.builder()
                //.id(dto.id()) // puede ser null, se generará UUID en el adaptador
                .idNumber(dto.idNumber())
                .name(dto.name())
                .lastname(dto.lastname())
                .birthdate(dto.birthdate())
                .address(dto.address())
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .baseSalary(dto.baseSalary())
                .rolId(dto.rolId())
                .password(dto.password())
                .build();
    }

    // De Dominio a DTO
    public static CreateUserDTO toDTO(UserDomain user) {
        return CreateUserDTO.builder()
                //.id(user.getId())
                .idNumber(user.getIdNumber())
                .name(user.getName())
                .lastname(user.getLastname())
                .birthdate(user.getBirthdate())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .password(user.getPassword())
                .baseSalary(user.getBaseSalary())
                .rolId(user.getRolId())
                .build();
    }
}
