package com.softserve.teachua.pages.home;

import com.softserve.teachua.data.home.BannerItem;
import com.softserve.teachua.pages.TopPart;
import com.softserve.teachua.tools.TextUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomeBannerComponent {
    private final String URL_PATTER_TEXT = "url\\(\"(https?://(\\w\\.)*\\w{2,}.*)\"\\)";

    private WebDriver driver;
    private WebElement searchDivElement;
    //
    private WebElement titleLabel;
    private WebElement description;
    private WebElement detailsButton;
    private WebElement detailsButtonLink;
    private WebElement pictureUrl;

    public HomeBannerComponent(WebDriver driver, WebElement searchDivElement) {
        this.driver = driver;
        this.searchDivElement = searchDivElement;
        initElements();
        //checkElements();
    }

    private void initElements() {
        // init elements
         titleLabel = searchDivElement.findElement(By.cssSelector("h2"));
         description = searchDivElement.findElement(By.cssSelector("span.description"));
         detailsButton = searchDivElement.findElement(By.cssSelector("button"));
         detailsButtonLink = detailsButton.findElement(By.xpath("./.."));
         pictureUrl = searchDivElement.findElement(By.cssSelector("div.carousel-item"));
    }

    // Page Object

    // searchDivElement;
    public WebElement getSearchDivElement() {
        return searchDivElement;
    }

    // titleLabel;
    public WebElement getTitleLabel() {
        return titleLabel;
    }

    public String getTitleLabelText() {
        return getTitleLabel().getText();
    }

    // description;
    public WebElement getDescription() {
        return description;
    }

    public String getDescriptionText() {
        return getDescription().getText();
    }

   // detailsButton;
   public WebElement getDetailsButton() {
        return detailsButton;
    }

    public String getDetailsButtonText() {
        return getDetailsButton().getText();
    }
    public String getDetailsButtonUrlText() {
        return getDetailsButtonLinkText();
    }

    public void clickDetailsButton() {
        getDetailsButton().click();
    }

    // detailsButtonLink
    public WebElement getDetailsButtonLink() {
        return detailsButtonLink;
    }

    public String getDetailsButtonLinkText() {
        return getDetailsButtonLink().getAttribute(TopPart.TAG_ATTRIBUTE_HREF);
    }

    // pictureUrl
    public WebElement getPictureUrl() {
        return pictureUrl;
    }

    public String getPictureUrlText() {
        String pictureUrl = null;
        String text = getPictureUrl().getAttribute(TopPart.TAG_ATTRIBUTE_STYLE);
        pictureUrl = TextUtils.unpackFirstGroupSubText(URL_PATTER_TEXT, text);
        return pictureUrl;
    }

    // Functional

    private <T extends TopPart> T createClazz(Class<T> clazz) {
        T result = null;
        try {
            result = clazz.getConstructor(WebDriver.class).newInstance(driver);
        } catch (Exception e) {
            // TODO Develop Custom Exception
            throw new RuntimeException(e);
        }
        return result;
    }

    // Business Logic

    public <T extends TopPart> T pressDetailsButton(BannerItem<T> bannerItem) {
        clickDetailsButton();
        return createClazz(bannerItem.getClazz());
    }

}
