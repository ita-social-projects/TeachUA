package com.softserve.teachua.tools.repository;

import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import java.util.Arrays;
import java.util.List;

public class BannerInfoRepository {
    public static List<BannerItemProfile> getBannersInfo() {
        return Arrays.asList(BannerItemProfile.builder().id(1L).link("/about").sequenceNumber(2)
                        .title("Навчай Українською")
                        .subtitle("Ініціатива \"Навчай українською\" - це небайдужі громадяни, які об'єдналися, щоб "
                                + "популяризувати українську мову у сфері освіти.")
                        .picture("data_for_db/banners/aboutUs.jpg").build(),
                BannerItemProfile.builder().id(2L).link("/clubs").sequenceNumber(1).title("Про гуртки українською")
                        .subtitle("На нашому сайті ви можете обрати для вашої дитини гурток, "
                                + "де навчають українською мовою.")
                        .picture("data_for_db/banners/aboutClubs.jpg").build(),
                BannerItemProfile.builder().id(3L).link("/challengeUA").sequenceNumber(3)
                        .title("Челендж \"Навчай українською\"для викладачів позашкільних закладів освіти")
                        .subtitle("21 день української мови для тренерів спортивних секцій та викладачів гуртків. "
                                + "Початок 5 листопада 2021 року.")
                        .picture("data_for_db/banners/maraton.jpg").build());
    }
}
