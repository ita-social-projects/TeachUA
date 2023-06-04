package com.softserve.certificate.constants;

/**
 * Enum class to refer to roles in code.
 */
public enum RoleData {
    ADMIN("ADMIN"), USER("USER"), MANAGER("MANAGER");

    private static final String DB_PREFIX = "ROLE_";
    private final String name;

    RoleData(String name) {
        this.name = name;
    }

    /**
     * Returns name of the role.
     *
     * @return name
     */
    public String getRoleName() {
        return name;
    }

    /**
     * Returns name of the role like it is in DB.
     *
     * @return ROLE_ + name
     */
    public String getDBRoleName() {
        return DB_PREFIX + name;
    }

    @Override
    public String toString() {
        return name;
    }
}
