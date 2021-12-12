package com.softserve.teachua.tools.transfer_repository;

import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface MoveBannerRepository {
    default public List<BannerItemProfile> getBanners() {
        return Arrays.asList(
                BannerItemProfile.builder().
                        id(1l)
                        .sequenceNumber(1)
                        .title("Навчай Українською")
                        .subtitle("Ініціатива")
                        .picture("/upload/target/banner_Images/aboutUs.jpg").build(),
                BannerItemProfile.builder()
                        .id(2l)
                        .link("/clubs")
                        .sequenceNumber(1)
                        .title("Про гуртки українською")
                        .subtitle("На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.")
                        .picture("/upload/target/banner_Images/aboutClubs.jpg").build(),
                BannerItemProfile.builder()
                        .id(3l)
                        .link("/challengeUA")
                        .sequenceNumber(2)
                        .title("Челендж \"Навчай українською\"для викладачів позашкільних закладів освіти")
                        .subtitle("21 день української мови для тренерів спортивних секцій та викладачів гуртків. Початок 5 листопада 2021 року.")
                        .picture("/upload/target/banner_Images/maraton.jpg").build(),
                BannerItemProfile.builder()
                        .id(4l)
                        .link("/about")
                        .sequenceNumber(3)
                        .title("Про нас")
                        .subtitle("Ініціатива \"Навчай українською\" - це небайдужі громадяни, які об'єдналися, щоб популяризувати українську мову у сфері освіти.")
                        .picture("/upload/target/banner_Images/aboutUs.jpg")
                        .build()
        );
    }
}
