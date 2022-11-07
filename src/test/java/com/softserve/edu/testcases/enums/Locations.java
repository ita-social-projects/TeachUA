package com.softserve.edu.testcases.enums;

public enum Locations {
    DEFAULT_LOCATION("Київ"),
    LVIV("Львів"),
    KYIV("Київ"),
    DNIPRO("Дніпро"),
    KHARKIV("Харків"),
    ODESA("Одеса"),
    ZAPORIZHZHIA("Запоріжжя"),
    RIVNE("Рівне"),
    LUTSK("Луцьк"),
    SOLONKA("Солонка"),
    BIBRKA("Бібрка");

    private String city;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    Locations(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return city;
    }
}
