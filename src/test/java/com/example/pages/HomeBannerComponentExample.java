package com.example.pages;

import com.softserve.teachua.pages.TopPart;
import com.softserve.teachua.tools.TextUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomeBannerComponentExample {
    private final String URL_PATTER_TEXT = "url\\(\"(https?://(\\w\\.)*\\w{2,}.*)\"\\)";

    private WebElement searchDivElement;
    //
    private WebElement titleLabel;
    private WebElement description;
    private WebElement detailsButton;
    private WebElement pictureUrl;

    public HomeBannerComponentExample(WebElement searchDivElement) {
        this.searchDivElement = searchDivElement;
        initElements();
        //checkElements();
    }

    private void initElements() {
        // init elements
         titleLabel = searchDivElement.findElement(By.cssSelector("h2"));
         description = searchDivElement.findElement(By.cssSelector("span.description"));
         detailsButton = searchDivElement.findElement(By.cssSelector("button"));
         pictureUrl = searchDivElement.findElement(By.cssSelector("div.carousel-item"));
    }

    // Page Object

    // searchDivElement;
    public WebElement getcSearchDivElement() {
        return searchDivElement;
    }

    // titleLabel;
    public WebElement getcTitleLabel() {
        return titleLabel;
    }

    public String getTitleLabelText() {
        return getcTitleLabel().getText();
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

    public void clickDetailsButton() {
        getDetailsButton().click();
    }

    // pictureUrl
    public WebElement getPictureUrl() {
        return pictureUrl;
    }

    public String getPictureUrlText() {
        String pictureUrl = null;
        String text = getPictureUrl().getAttribute(TopPart.TAG_ATTRIBUTE_STYLE);
        pictureUrl = TextUtils.unpackFirstSubText(URL_PATTER_TEXT, text, 1);
        return pictureUrl;
    }


    // Functional

    // Business Logic
}
