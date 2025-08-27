package co.com.crediyareactivo.api.helpers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordHashService {
    private final PasswordEncoder passwordEncoder;

    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
