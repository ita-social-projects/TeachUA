package com.softserve.teachua.constants;

public enum RoleData {
    ADMIN("ADMIN"),
    USER("USER"),
    MANAGER("MANAGER");

    private final String DB_PREFIX = "ROLE_";
    private String name;

    private RoleData(String name) {
        this.name = name;
    }

    public String getRoleName() {
        return name;
    }

    public String getDBRoleName() {
        return DB_PREFIX + name;
    }

    @Override
    public String toString() {
        return name;
    }
}
