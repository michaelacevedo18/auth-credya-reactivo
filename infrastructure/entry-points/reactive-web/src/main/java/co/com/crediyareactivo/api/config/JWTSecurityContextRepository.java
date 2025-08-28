package co.com.crediyareactivo.api.config;


import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import co.com.crediyareactivo.model.user.gateways.ports.JWTServicePort;
import co.com.crediyareactivo.model.user.models.UserResponseDomain;
import reactor.core.publisher.Mono;



@Component
public class JWTSecurityContextRepository implements ServerSecurityContextRepository{
    private final JWTServicePort jwtService;

    public JWTSecurityContextRepository(JWTServicePort jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.empty();
        }

        String token = authHeader.substring(7);

        try {
            UserResponseDomain claims = jwtService.validateToken(token);
            String role = "ROLE_" + claims.getRolName();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    claims.getEmail(),
                    null,
                    List.of(new SimpleGrantedAuthority(role))
            );

            return Mono.just(new SecurityContextImpl(auth));
        } catch (Exception e) {
            return Mono.empty();
        }
    }
}