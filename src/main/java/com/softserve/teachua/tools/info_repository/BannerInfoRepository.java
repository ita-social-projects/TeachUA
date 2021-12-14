package com.softserve.teachua.tools.info_repository;

import com.softserve.teachua.dto.banner_item.BannerItemProfile;

import java.util.Arrays;
import java.util.List;

public class BannerInfoRepository {
    public static List<BannerItemProfile> getBannersInfo() {
        return Arrays.asList(
                BannerItemProfile.builder().
                        id(1L)
                        .sequenceNumber(1)
                        .title("Навчай Українською")
                        .subtitle("Ініціатива")
                        .picture("banner_image_for_db/aboutUs.jpg").build(),
                BannerItemProfile.builder()
                        .id(2L)
                        .link("/clubs")
                        .sequenceNumber(1)
                        .title("Про гуртки українською")
                        .subtitle("На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.")
                        .picture("banner_image_for_db/aboutClubs.jpg").build(),
                BannerItemProfile.builder()
                        .id(3L)
                        .link("/challengeUA")
                        .sequenceNumber(2)
                        .title("Челендж \"Навчай українською\"для викладачів позашкільних закладів освіти")
                        .subtitle("21 день української мови для тренерів спортивних секцій та викладачів гуртків. Початок 5 листопада 2021 року.")
                        .picture("banner_image_for_db/maraton.jpg").build(),
                BannerItemProfile.builder()
                        .id(4L)
                        .link("/about")
                        .sequenceNumber(3)
                        .title("Про нас")
                        .subtitle("Ініціатива \"Навчай українською\" - це небайдужі громадяни, які об'єдналися, щоб популяризувати українську мову у сфері освіти.")
                        .picture("banner_image_for_db/aboutUsMainPage.jpg")
                        .build()
        );
    }
}
