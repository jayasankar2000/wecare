package cfp.wecare.model;

import lombok.Getter;

@Getter
public enum Role {

    SUPER_ADMIN(RoleConstants.SUPER_ADMIN),
    ORG_ADMIN(RoleConstants.ORG_ADMIN),
    SIMPLE_USER(RoleConstants.SIMPLE_USER);

    private final String role;

    private Role(final String role) {
        this.role = role;
    }

    public static class RoleConstants {
        public static final String SUPER_ADMIN = "SUPER_ADMIN";
        public static final String ORG_ADMIN = "ORG_ADMIN";
        public static final String SIMPLE_USER = "SIMPLE_USER";
    }

}
