package com.softserve.teachua.data.home;

import com.softserve.teachua.pages.AboutUsPage;
import com.softserve.teachua.pages.ClubPage;
import com.softserve.teachua.pages.TopPart;

public final class BannerRepository {

    private BannerRepository() {
    }

    public static BannerItem getDefault() {
        return getFirst();
    }

    public static BannerItem getInvalid() {
        return new BannerItem("Invalid","Invalid", "http://", "http://", TopPart.class);
    }

    public static BannerItem getNew(int number) {
        return new BannerItem("Valid Info","Valid Info",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banners/2021-12-19_13-02-29_maraton.jpg",
                TopPart.class)
                .setUrlLocalPicture("C:/temp/my.jpg")
                .setNumber(number);
    }

    public static <T extends TopPart> BannerItem getNew(int number, Class<T> clazz) {
        return new BannerItem("Valid Info","Valid Info",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banners/2021-12-19_13-02-29_maraton.jpg",
                clazz)
                .setUrlLocalPicture("C:/temp/my.jpg")
                .setNumber(number);
    }

    public static BannerItem getFirst() {
        return new BannerItem("Тестовий банер",
                "Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banners/2021-12-19_13-02-29_maraton.jpg",
                ClubPage.class)
                .setNumber(0);
    }

    public static BannerItem getSecond() {
        return new BannerItem("Гуртки українською",
                "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banner/2021-12-19_13-02-29_aboutClubs.jpg",
                ClubPage.class)
                .setNumber(1);
    }
    public static BannerItem getLast() {
        return new BannerItem("Тест Тест",
                "Тест Тес 11",
                "https://speak-ukrainian.org.ua/dev/challenges/234",
                "https://speak-ukrainian.org.ua/dev/upload/banners/1Jedyni.png",
                AboutUsPage.class) // TODO Update to ChallengePage
                .setNumber(8);
    }

    // TODO
    public static BannerBox getBannerBox() {
        return new BannerBox(9)
                .addBanner(getFirst())
                .addBanner(getSecond());
    }

     /*-
    public List<BannerItem> fromCsv(String filename) {
        return BannerItem.getByLists(new CSVReader(filename).getAllCells());
    }

    public List<BannerItem> fromCsv() {
        return fromCsv("users.csv");
    }

    public List<BannerItem> fromExcel(String filename) {
        return BannerItem.getByLists(new ExcelReader(filename).getAllCells());
    }

    public List<BannerItem> fromExcel() {
        return fromExcel("users.xlsx");
    }
    */

}
