package com.softserve.teachua.data.home;

import java.util.ArrayList;
import java.util.List;

public class BannerBox {
    private final String ITEM_NOT_FOUND = "count = %s, out of bounds";

    private List<BannerItem> banners;
    private int count;

    public BannerBox(int count) {
        this.banners = new ArrayList<>();
        this.count = count;
    }

    public BannerBox addBanner(BannerItem bannerItem) {
        banners.add(bannerItem);
        return this;
    }

    public List<BannerItem> getBanners() {
        return banners;
    }

    public BannerItem getBannerByNumber(int number) {
        BannerItem banner = null;
        if (number < banners.size()) {
            banner = getBanners().get(number);
        } else {
            // TODO Develop Custom Exception
            throw new RuntimeException(String.format(ITEM_NOT_FOUND, number));
        }
        return banner;
    }

    public int getCount() {
        return count;
    }

}
