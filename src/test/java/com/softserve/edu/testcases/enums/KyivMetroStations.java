package com.softserve.edu.testcases.enums;

public enum KyivMetroStations {
    DEFAULT_LOCATION("Арсенальна"),
    ARSENALNA("Арсенальна"),
    BERESTEYSKA("Берестейська"),
    BORYSPILSKA("Бориспільська"),
    VASYLKIVSKA("Васильківська"),
    VOKZALNA("Вокзальна"),
    VYDUBYCHI("Видубичі"),
    VYRLITSA("Вирлиця"),
    VYSTAVKOVYY_CENTER("Виставковий центр"),
    HEROYIV_DNIPRA("Героїв Дніпра"),
    HOLOSIIVSKA("Голосіївська"),
    DARNYTSYA("Дарниця"),
    PALATS_SPORTU("Палац Спорту"),
    UKRAINE_PALACE("Палац Україна"),
    DEMIIVSKA("Деміївська"),
    DNIPRO("Дніпро"),
    DOROHOZHYCHI("Дорогожичі"),
    DRUZHBY_NARODIV("Дружби народів"),
    ZHYTOMYRSKA("Житомирська"),
    ZOLOTI_VOROTA("Золоті ворота"),
    IPODROM("Іподром"),
    KLOVSKA("Кловська"),
    KONTRAKTOVA_PLOSHCHA("Контрактова площа"),
    KHRESHCHATYK("Хрещатик"),
    LIVOBEREZHNA("Лівобережна"),
    LISOVA("Лісова"),
    LUKYANIVSKA("Лук'янівська"),
    LYBIDSKA("Либідська"),
    MAYDAN_NEZALEZHNOSTI("Майдан Незалежності"),
    MINSKA("Мінська"),
    NYVKY("Нивки"),
    OBOLON("Оболонь"),
    OLIMPIYSKA("Олімпійська"),
    OSOKORKY("Осокорки"),
    PECHERSKA("Печерська"),
    PLOSHCHA_LVA_TOLSTOHO("Площа Льва Толстого"),
    POZNYAKY("Позняки"),
    POLYTECHNIC_INSTITUTE("Політехнічний інститут"),
    POCHAINA("Почайна"),
    POSHTOVA_PLOSHCHA("Поштова площа"),
    SVYATOSHYN("Святошин"),
    SLAVUTYCH("Славутич"),
    SYRETS("Сирець"),
    TARAS_SHEVCHENKO("Тараса Шевченка"),
    TEATRALNA("Театральна"),
    TEREMKY("Теремки"),
    UNIVERSITY("Університет"),
    KHARKIVSKA("Харківська"),
    CHERNIHIVSKA("Чернігівська"),
    SHULYAVSKA("Шулявська"),
    VOSKRESENKA("Воскресенка"),
    TROYESHCHYNA("Троєщина"),
    KURENIVKA("Куренівка"),
    CHOKOLIVKA("Чоколівка"),
    BORSCHAHIVKA("Борщагівка"),
    VYNOHRADAR("Виноградар"),
    KOTSYUBYNSKE("Коцюбинське"),
    KOLESO_VITRIV("Колесо вітрів"),
    AKADEMMISTECHKO("Академмістечко");

    private String station;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    KyivMetroStations(String station) {
        this.station = station;
    }

    @Override
    public String toString() {
        return station;
    }

}
