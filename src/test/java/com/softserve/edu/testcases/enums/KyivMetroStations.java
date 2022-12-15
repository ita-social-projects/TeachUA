package com.softserve.edu.testcases.enums;

public enum KyivMetroStations {
    DEFAULT_LOCATION("Арсенальна"),
    ARSENALNA("Арсенальна"),
    VYRLITSA("Вирлиця"),
    BORYSPILSKA("Бориспільська"),
    BERESTEYSKA("Берестейська");

    private final String station;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    KyivMetroStations(String station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return station;
    }

}
