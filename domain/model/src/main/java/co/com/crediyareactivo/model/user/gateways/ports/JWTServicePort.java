package co.com.crediyareactivo.model.user.gateways.ports;


import co.com.crediyareactivo.model.user.models.UserResponseDomain;

public interface JWTServicePort {
    String generateToken(String email, Long rolId);
    //Claims validateToken(String token);
    UserResponseDomain validateToken(String token);
}
