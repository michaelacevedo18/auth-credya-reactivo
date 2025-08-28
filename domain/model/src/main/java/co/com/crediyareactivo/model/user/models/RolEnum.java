package co.com.crediyareactivo.model.user.models;

public enum RolEnum {
     ADMIN(1L, "ADMIN"),
    ASESOR(2L, "ASESOR"),
    CLIENTE(3L, "CUSTOMER");

    private final Long id;
    private final String name;

    RolEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static String getNameById(Long id) {
        for (RolEnum rol : values()) {
            if (rol.id.equals(id)) {
                return rol.name;
            }
        }
        throw new IllegalArgumentException("Rol desconocido: " + id);
    }
}
