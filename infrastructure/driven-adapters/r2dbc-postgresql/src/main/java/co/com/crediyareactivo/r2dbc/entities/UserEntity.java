package co.com.crediyareactivo.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("users")
public class UserEntity {
    @Id
    private String id;

    @Column("idNumber")
    private Long idNumber;

    @Column("name")
    private String name;

    @Column("last_name")
    private String lastname;

     @Column("birthDate")
    private LocalDate birthdate;

    @Column("address")
    private String address;

    @Column("phoneNumber")
    private String phoneNumber;

    @Column("email")
    private String email;

    @Column("baseSalary")
    private Double baseSalary;

    @Column("rolId")
    private Long rolId;
}
