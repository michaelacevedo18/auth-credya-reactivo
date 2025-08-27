package co.com.crediyareactivo.model.user.models;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserResponseDomain {
    private String token;
    private String email;
    private Long idNumber;
    private String rolName;
}
