package com.softserve.teachua.data.home;

public final class BannerRepository {

    private BannerRepository() {
    }

    public static BannerItem getDefault() {
        return getFirst();
    }

    public static BannerItem getInvalid() {
        return new BannerItem("Invalid","Invalid", "http://", "http://");
    }

    public static BannerItem getNew() {
        return new BannerItem("Valid Info","Valid Info",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banners/2021-12-19_13-02-29_maraton.jpg")
                .setUrlLocalPicture("C:/temp/my.jpg")
                .setNumber(10);
    }

    public static BannerItem getFirst() {
        return new BannerItem("Тестовий банер",
                "Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий банер 091Тестовий",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banners/2021-12-19_13-02-29_maraton.jpg")
                .setNumber(0);
    }

    public static BannerItem getSecond() {
        return new BannerItem("Гуртки українською",
                "На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.",
                "https://speak-ukrainian.org.ua/dev/clubs",
                "https://speak-ukrainian.org.ua/dev/upload/banner/2021-12-19_13-02-29_aboutClubs.jpg")
                .setNumber(1);
    }
    public static BannerItem getLast() {
        return new BannerItem("Тест Тест",
                "Тест Тес 11",
                "https://speak-ukrainian.org.ua/dev/challenges/234",
                "https://speak-ukrainian.org.ua/dev/upload/banners/1Jedyni.png")
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
