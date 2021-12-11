package com.softserve.teachua.tools.transfer_service.transfer_impl;

import com.softserve.teachua.dto.banner_item.BannerItemProfile;
import com.softserve.teachua.dto.banner_item.SuccessCreatedBannerItem;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
import com.softserve.teachua.tools.transfer_service.BannerItemTransferService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class BannerItemTransferServiceImpl implements BannerItemTransferService {

    final BannerItemServiceImpl bannerItemService;

    public BannerItemTransferServiceImpl(BannerItemServiceImpl bannerItemService) {
        this.bannerItemService = bannerItemService;
    }

    @Override
    public List<SuccessCreatedBannerItem> moveBannerToDB() {
        List<SuccessCreatedBannerItem> bannerList = new LinkedList<>();
        String pathToResources = "./src/main/resources/banner_image_for_db/";
        String pathToImages = "./target/banner_Images";

        FileUtils.listFiles(new File(pathToResources), null, false)
                .forEach(file -> {
                    try {
                        FileUtils.moveToDirectory(new File(pathToResources + "/" + file.getName())
                                , new File(pathToImages),
                                true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        BannerItemProfile aboutUsBanner = BannerItemProfile.builder().
                id(1l)
                .sequenceNumber(1)
                .title("Навчай Українською")
                .subtitle("Ініціатива")
                .picture("./target/banner_Images/aboutUs.jpg")
                .build();
        BannerItemProfile mainPageBannerNo1 = BannerItemProfile.builder()
                .id(2l)
                .link("/clubs")
                .sequenceNumber(1)
                .title("Про гуртки українською")
                .subtitle("На нашому сайті ви можете обрати для вашої дитини гурток, де навчають українською мовою.")
                .picture("./target/banner_Images/aboutClubs.jpg").build();
        BannerItemProfile mainPageBannerNo2 = BannerItemProfile.builder()
                .id(3l)
                .link("/challengeUA")
                .sequenceNumber(2)
                .title("Челендж \"Навчай українською\"для викладачів позашкільних закладів освіти")
                .subtitle("21 день української мови для тренерів спортивних секцій та викладачів гуртків. Початок 5 листопада 2021 року.")
                .picture("/target/banner_Images/maraton.jpg")
                .build();
        BannerItemProfile mainPageBannerNo3 = BannerItemProfile.builder()
                .id(4l)
                .link("/about")
                .sequenceNumber(3)
                .title("Про нас")
                .subtitle("Ініціатива \"Навчай українською\" - це небайдужі громадяни, які об'єдналися, щоб популяризувати українську мову у сфері освіти.")
                .picture("./target/banner_Images/aboutUs.jpg")
                .build();

        bannerList.add(bannerItemService.addBannerItem(aboutUsBanner));
        bannerList.add(bannerItemService.addBannerItem(mainPageBannerNo1));
        bannerList.add(bannerItemService.addBannerItem(mainPageBannerNo2));
        bannerList.add(bannerItemService.addBannerItem(mainPageBannerNo3));

        return bannerList;
    }
}
