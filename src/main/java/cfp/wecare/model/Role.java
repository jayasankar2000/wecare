package cfp.wecare.model;

public enum Role {

    SUPER_ADMIN(RoleConstants.SUPER_ADMIN),
    ORG_ADMIN(RoleConstants.ORG_ADMIN),
    SIMPLE_USER(RoleConstants.SIMPLE_USER);

    private final String role;

    private Role(final String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public static class RoleConstants {
        public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
        public static final String ORG_ADMIN = "ROLE_ORG_ADMIN";
        public static final String SIMPLE_USER = "ROLE_SIMPLE_USER";
    }

}
