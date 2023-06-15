package com.softserve.teachua.utils.viberbot.constants;

public class TextConstants {
    public static final String AUTH = "Пройти автентифікацію";
    public static final String AUTH_MESSAGE = "Вас вітає вайбер бот Говоримо українською. " +
            "Поки що ми не знаємо хто Ви, тому пропонуємо пройти процес автентифікації.";
    public static final String AUTH_FAILED_MESSAGE = "На жаль за вашими даними не знайдено профілю, " +
            "спробуйте ввести інформацію ще раз";
    public static final String AUTH_EMAIL_MESSAGE = "Будь ласка, введіть адрес " +
            "електронної пошти, котрий Ви вводили при проходженні курсів.";
    public static final String AUTH_NAME_MESSAGE = "Введіть будь ласка ПІБ які Ви вводили при проходженні курсів.";
    public static final String CERT_PREFIX = "Сертифікат: ";
    public static final String BACK = "Повернутися назад";
    public static final String START_CONV = "Почати";
    public static final String MENU_INFO = "Інформація";
    public static final String MENU_CERTIFICATES = "Сертифікати";
    public static final String MENU_MENU = "Головне меню";
    public static final String MENU_MESSAGE = "" +
            "Вас вітає бот проекту Говоримо українською. " +
            "Поки що функціонал перебуває у тестовому режимі, проте уже зовсім скоро усе запрацює)";

    public static final String WELCOME_MESSAGE = "" +
            "Вас вітає бот проекту Говоримо українською. " +
            "Для того щоп почати користуватися ботом натисніть кнопку почати.";

    public static final String INFO_MESSAGE = "" +
            "Розділ інформації. " +
            "Поки що функціонал перебуває у тестовому режимі, проте уже зовсім скоро усе запрацює)";

    public static final String CERT_MESSAGE = "" +
            "Виберіть сертифікат доступний для завантаження";

    public static final String NOT_FOUND_CERT_MESSAGE = "На жаль ми не змогли знайти доступні для завантаження " +
            "сертифікати.";

    public static final String CERTIFICATE_REQUEST_MESSAGE = "Ваш запит надісланий в обробку, очікуйте на " +
            "сертифікат.";

    private TextConstants() {
        throw new UnsupportedOperationException("This class should be used statically");
    }
}
