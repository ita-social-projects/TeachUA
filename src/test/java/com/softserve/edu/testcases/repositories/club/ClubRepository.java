package com.softserve.edu.testcases.repositories.club;

public final class ClubRepository {

    private ClubRepository() {
    }

    public static Club getDefault() {
        return getJeromeITSchool();
    }

    public static Club getJeromeITSchool() {
        return Club.get()
                .setTitle("Jerome IT School")                           // set title
                .setCategory("Програмування, робототехніка, STEM")      // set category
                .setDescription("Опис відсутній")                       // set description
                .setRate(5)                                             // set rate
                .setLocation("string")                                  // set location
                // Call build method which returns device object to build all
                .build();
    }

    public static Club getGreenCountry() {
        return Club.get()
                .setTitle("Грін Кантрі")                                // set title
                .setCategory("Інше")                                    // set category
                .setDescription("Мережа шкіл англійської мови для " +   // set description
                        "дітей, навчання за концепцією ''Перевернутий " +
                        "Урок'', унікальна система мотивація учнів " +
                        "(шкільна валюта та крамничка), власна освітня " +
                        "платформа. Поза англійською використовується " +
                        "лише українська мова.")
                .setRate(0)                                             // set rate
                .setLocation("вул. М.Гришка, 6А")                       // set location
                // Call build method which returns device object to build all
                .build();
    }

}
