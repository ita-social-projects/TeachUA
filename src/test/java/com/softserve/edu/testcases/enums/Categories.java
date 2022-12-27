package com.softserve.edu.testcases.enums;

public enum Categories {
    DEFAULT_CATEGORY("Спортивні секції"),
    SPORT_SECTIONS("Спортивні секції"),
    DANCING("Танці, хореографія"),
    EARLY_DEVELOPMENT("Студії раннього розвитку"),
    PROGRAMMING("Програмування, робототехніка, STEM"),
    MUSIC_RELATED("Вокальна студія, музика, музичні інструменти"),
    THEATER("Акторська майстерність, театр"),
    STUDY_CHILDREN("Вчіться, діти"),
    BASICS("Основи"),
    JAVA_BASICS("Основи Java444"),
    PERSONAL_GROWTH("Особистісний розвиток"),
    CHILD_TV("Журналістика, дитяче телебачення, монтаж відео, влогів"),
    DEVELOPMENT_CENTER("Центр розвитку"),
    OTHER("Інше");

    private final String category;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    Categories(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }

}
