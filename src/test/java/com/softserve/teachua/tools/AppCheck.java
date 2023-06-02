package com.softserve.teachua.tools;

public class AppCheck {
    public static void main(String[] args) {
        /*
        String text = "style=\"background: linear-gradient(rgba(0, 0, 0, 0.525), rgba(0, 0, 0, 0.5)), url(\"https://speak-ukrainian.org.ua/dev/upload/banners/2021-12-19_13-02-29_maraton.jpg\") 50% 28% / cover no-repeat;\"";
        //String pattern = "url\\(\"(https://(\\w\\.)*\\w{2,}(/\\w+)+\\.\\w{3,4})\"\\)";
        //String pattern = "url\\(\"(https://.*)\"\\)";
        String pattern = "url\\(\"(https?://(\\w\\.)*\\w{2,}.*)\"\\)";
        System.out.println("res = " + TextUtils.unpackFirstSubText(pattern, text));
        System.out.println("res group = " + TextUtils.unpackFirstSubText(pattern, text, 1));
        */
        //
        String text = "Optional[public void com.softserve.teachua.tests.service.home.HomeBannerFunctionTest.checkHomeBannerPictureUrl(com.softserve.teachua.data.home.BannerItem)]";
        String pattern = ".+\\.(\\w+)\\(.+";
        System.out.println("res = " + TextUtils.unpackFirstSubText(pattern, text, 1));
    }
}
