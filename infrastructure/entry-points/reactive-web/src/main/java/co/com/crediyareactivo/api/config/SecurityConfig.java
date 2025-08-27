package co.com.crediyareactivo.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable()) // aún necesario aunque esté deprecado
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/api/v1/auth/**" // login y registro sin autenticación
                        ).permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/users").hasAnyAuthority("1", "2") // admin o asesor
                        .pathMatchers(HttpMethod.POST, "/api/v1/loan").hasAuthority("3") // cliente
                        .anyExchange().authenticated()
                )
                .build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
