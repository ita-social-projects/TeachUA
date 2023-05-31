package com.softserve.teachua.pages.home;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class HomeBannerContainer {
    private final String TAG_ATTRIBUTE_CLASS = "class";
    private final String SLICK_ACTIVE_CLASS_TEXT = "slick-active";

    //private final String HOME_BANNER_DIV_ELEMENT_CSS_SELECTOR = "div.about-carousel-block div.slick-slide:not(.slick-cloned)";
    private final String HOME_BANNER_DIV_ELEMENT_CSS_SELECTOR = "div.about-carousel-block div.slick-slide.slick-active.slick-current";
    private final String SLICK_DOTS_BOTTOMS_CSS_SELECTOR = "div.about-carousel-block  ul.slick-dots.slick-dots-bottom li";

    private final String HOME_BANNERNOT_FOUND = "homeBanner not Found.";
    private final String HOME_BANNER_TITLE_NOT_FOUND = "homeBannerTitle: %s not Found.";
    //
    protected WebDriver driver;
    //
    private List<HomeBannerComponent> homeBannerComponents;

    private List<WebElement> slickDotsBottoms;

    private int homeBannerPosition;

    public HomeBannerContainer(WebDriver driver) {
        this.driver = driver;
        initElements();
    }

    private void initElements() {
        // init elements
        homeBannerComponents = new ArrayList<>();
        homeBannerPosition = -1;
        for (WebElement current : driver.findElements(By.cssSelector(HOME_BANNER_DIV_ELEMENT_CSS_SELECTOR))) {
            homeBannerComponents.add(new HomeBannerComponent(current));
        }
        slickDotsBottoms = new ArrayList<>();
        for (WebElement current : driver.findElements(By.cssSelector(SLICK_DOTS_BOTTOMS_CSS_SELECTOR))) {
            slickDotsBottoms.add(current);
            if ((current.getAttribute(TAG_ATTRIBUTE_CLASS) != null)
                && (!current.getAttribute(TAG_ATTRIBUTE_CLASS).isEmpty())
                && (current.getAttribute(TAG_ATTRIBUTE_CLASS).equals(SLICK_ACTIVE_CLASS_TEXT))) {
                homeBannerPosition = slickDotsBottoms.size() - 1;
            }
        }
        //
        if (homeBannerComponents.size() == 0) {
            // TODO Develop Custom Exception
            throw new RuntimeException(HOME_BANNERNOT_FOUND);
        }
    }

    // Page Object

    // homeBannerComponents
    public List<HomeBannerComponent> getHomeBannerComponents() {
        return homeBannerComponents;
    }

    // slickDotsBottoms
    public List<WebElement> getSlickDotsBottoms() {
        return slickDotsBottoms;
    }

    protected void clickSlickDotsBottomsByNumber(int number) {
        getSlickDotsBottoms().get(number).click();
    }

    // homeBannerPosition
    public int getHomeBannerPosition() {
        return homeBannerPosition;
    }

    // Functional

    public int getHomeBannerComponentsCount() {
        return getHomeBannerComponents().size();
    }

    public int getSlickDotsBottomsCount() {
        return getSlickDotsBottoms().size();
    }

    public List<String> getHomeBannerComponentTitles() {
        List<String> homeBannerComponentTitles = new ArrayList<>();
        for (HomeBannerComponent current : getHomeBannerComponents()) {
            homeBannerComponentTitles.add(current.getTitleLabelText());
            //System.out.println("\ncurrent.get ... isDisplayed() " + current.getcSearchDivElement().isDisplayed());
        }
        return homeBannerComponentTitles;
    }

    /*
    // Move to test
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
    */

    public HomeBannerComponent getHomeBannerComponentByTitle(String homeBannerTitle) {
        HomeBannerComponent result = null;
        for (HomeBannerComponent current : getHomeBannerComponents()) {
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

    public HomeBannerComponent getHomeBannerComponentByPartialTitle(String homeBannerPartialTitle) {
        HomeBannerComponent result = null;
        for (HomeBannerComponent current : getHomeBannerComponents()) {
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

    /*
    // Move to test
    public void chooseHomeBannerComponentByNumber(int number) {
        clickSlickDotsBottomsByNumber(number);
    }

    public void chooseHomeBannerComponentByTitle(String homeBannerTitle) {
        clickSlickDotsBottomsByNumber(getHomeBannerComponentByTitlePosition(homeBannerTitle));
    }
    */

}
