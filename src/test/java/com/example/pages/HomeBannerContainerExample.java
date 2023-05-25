package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class HomeBannerContainerExample {

    private final String HOME_BANNER_DIV_ELEMENT_CSSSELECTOR = "div.about-carousel-block div.slick-slide:not(.slick-cloned)";
    private final String SLICK_DOTS_BOTTOMS_CSSSELECTOR = "div.about-carousel-block  ul.slick-dots.slick-dots-bottom li";

    private final String HOME_BANNER_TITLE_NOT_FOUND = "homeBannerTitle: %s not Found.";
    //
    protected WebDriver driver;
    //
    private List<HomeBannerComponentExample> homeBannerComponents;
    private List<WebElement> slickDotsBottoms;

    public HomeBannerContainerExample(WebDriver driver) {
        this.driver = driver;
        initElements();
    }

    private void initElements() {
        // init elements
        homeBannerComponents = new ArrayList<>();
        for (WebElement current : driver.findElements(By.cssSelector(HOME_BANNER_DIV_ELEMENT_CSSSELECTOR))) {
            homeBannerComponents.add(new HomeBannerComponentExample(current));
        }
        slickDotsBottoms = new ArrayList<>();
        for (WebElement current : driver.findElements(By.cssSelector(SLICK_DOTS_BOTTOMS_CSSSELECTOR))) {
            slickDotsBottoms.add(current);
        }
    }

    // Page Object

    // homeBannerComponents
    public List<HomeBannerComponentExample> getHomeBannerComponents() {
        return homeBannerComponents;
    }

    // slickDotsBottoms
    public List<WebElement> getSlickDotsBottoms() {
        return slickDotsBottoms;
    }

    protected void clickSlickDotsBottomsByNumber(int number) {
        getSlickDotsBottoms().get(number).click();
    }

    // Functional

    public int getHomeBannerComponentsCount() {
        return getHomeBannerComponents().size();
    }

    public List<String> getHomeBannerComponentTitles() {
        List<String> homeBannerComponentTitles = new ArrayList<>();
        for (HomeBannerComponentExample current : getHomeBannerComponents()) {
            homeBannerComponentTitles.add(current.getTitleLabelText());
        }
        return homeBannerComponentTitles;
    }

    public int getHomeBannerComponentByTitlePosition(String homeBannerTitle) {
        int position = -1;
        for (int i = 0; i < getHomeBannerComponentTitles().size(); i++) {
            if (getHomeBannerComponentTitles().get(i).toLowerCase().equals(homeBannerTitle.toLowerCase())) {
                position = i;
                break;
            }
        }
        if (position == -1) {
            // TODO Develop Custom Exception
            throw new RuntimeException(String.format(HOME_BANNER_TITLE_NOT_FOUND, homeBannerTitle));
        }
        return position;
    }

    public HomeBannerComponentExample getHomeBannerComponentByTitle(String homeBannerTitle) {
        HomeBannerComponentExample result = null;
        for (HomeBannerComponentExample current : getHomeBannerComponents()) {
            if (current.getTitleLabelText().toLowerCase()
                    .equals(homeBannerTitle.toLowerCase())) {
                result = current;
                break;
            }
        }
        if (result == null) {
            // TODO Develop Custom Exception
            // Use String.format()
            throw new RuntimeException(String.format(HOME_BANNER_TITLE_NOT_FOUND, homeBannerTitle));
        }
        return result;
    }

    public HomeBannerComponentExample getHomeBannerComponentByPartialTitle(String homeBannerPartialTitle) {
        HomeBannerComponentExample result = null;
        for (HomeBannerComponentExample current : getHomeBannerComponents()) {
            if (current.getTitleLabelText().toLowerCase()
                    .contains(homeBannerPartialTitle.toLowerCase())) {
                result = current;
                break;
            }
        }
        if (result == null) {
            // TODO Develop Custom Exception
            // Use String.format()
            throw new RuntimeException(String.format(HOME_BANNER_TITLE_NOT_FOUND, homeBannerPartialTitle));
        }
        return result;
    }

    // TODO Move to HomeBannerComponent
    public String getHomeBannerComponentDescriptionByTitle(String homeBannerTitle)
    //public String getHomeBannerComponentDescriptionByTitle(Product productName)
    {
        return getHomeBannerComponentByTitle(homeBannerTitle).getDescriptionText();
    }

    // TODO Move to HomeBannerComponent
    public void clickDetailsButtonByHomeBannerComponentByTitle(String homeBannerTitle) {
        getHomeBannerComponentByTitle(homeBannerTitle).clickDetailsButton();
    }

    // Business Logic

    public void chooseHomeBannerComponentByNumber(int number) {
        clickSlickDotsBottomsByNumber(number);
    }

    public void chooseHomeBannerComponentByTitle(String homeBannerTitle) {
        clickSlickDotsBottomsByNumber(getHomeBannerComponentByTitlePosition(homeBannerTitle));
    }

}
