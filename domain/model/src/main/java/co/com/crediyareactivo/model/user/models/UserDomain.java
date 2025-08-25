package co.com.crediyareactivo.model.user.models;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserDomain {
    private String id;
    private String name;
    private String lastname;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
    private String email;
    private Double baseSalary;
    private Long rolId; // ✅ Solo el ID del rol
    private Long idNumber;
}
