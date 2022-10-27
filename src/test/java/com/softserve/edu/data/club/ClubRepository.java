package com.softserve.edu.data.club;

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

    public static Club getAmericanGymnastics() {
        return Club.get()
                .setTitle("American Gymnastics Club")                   // set title
                .setCategory("Спортивні секції")                        // set category
                .setDescription("Американський гімнастичний клуб " +    // set description
                        "(American Gymnastics Club) – перша та єдина " +
                        "в країні мережа унікальних спортивних клубів, " +
                        "яка базується на Розвивальній Гімнастиці. Крім")
                .setRate(4)                                             // set rate
                .setLocation("Київ, вулиця Фізкультури 1, корпус 3")    // set location
                // Call build method which returns device object to build all
                .build();
    }

    public static Club getITEducationGrand() {
        return Club.get()
                .setTitle("IT освіта: курси \"ГРАНД\"")                 // set title
                .setCategory("Спортивні секції")                        // set category
                .setDescription("Ми вивчаємо все, що можна уявити в " + // set description
                        "ІТ і навіть більше. Загалом ми вчимо 20 " +
                        "тем. Всі ці теми ми вивчаємо в одному курсі, " +
                        "бо всі сучасні грамотні люди мають це знати. " +
                        "Ми набираєм")
                .setRate(3)                                             // set rate
                .setLocation("")                                        // set location
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
