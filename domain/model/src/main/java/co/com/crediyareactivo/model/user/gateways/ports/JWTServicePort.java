package co.com.crediyareactivo.model.user.gateways.ports;


import co.com.crediyareactivo.model.user.models.UserResponseDomain;

public interface JWTServicePort {
    String generateToken(String email, String rolName, String idNumber);
    UserResponseDomain validateToken(String token);
}
