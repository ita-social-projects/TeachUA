package com.softserve.edu.testcases.enums;

public enum KyivDistricts {
    DEFAULT_LOCATION("Деснянський"),
    DESNYANSKYI("Деснянський"),
    SVYATOSHINSKY("Святошинський");

    private final String district;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    KyivDistricts(String district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return district;
    }

}
