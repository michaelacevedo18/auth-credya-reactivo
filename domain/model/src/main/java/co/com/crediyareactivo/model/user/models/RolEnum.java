package co.com.crediyareactivo.model.user.models;

import java.util.Arrays;

public enum RolEnum {
    ADMIN(1), CUSTOMER(2);

    private final int id;

    RolEnum(int id) { this.id = id; }

    public static String fromId(int id) {
        return Arrays.stream(values())
                .filter(r -> r.id == id)
                .findFirst()
                .map(Enum::name)
                .orElse("UNKNOWN");
    }
}
