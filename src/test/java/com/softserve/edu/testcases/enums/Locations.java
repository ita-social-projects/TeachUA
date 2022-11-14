package com.softserve.edu.testcases.enums;

public enum Locations {
    DEFAULT_LOCATION("Київ"),
    KYIV("Київ"),
    KHARKIV("Харків");

    private final String city;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    Locations(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return city;
    }
}
