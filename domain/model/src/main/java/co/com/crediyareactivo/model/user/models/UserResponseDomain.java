package co.com.crediyareactivo.model.user.models;
import lombok.*;

@Data
@Builder
public class UserResponseDomain {
    private String token;
    private String email;
    private Long idNumber;
    private String rolName;
}
