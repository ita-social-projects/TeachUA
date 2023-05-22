package com.softserve.teachua.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class HomeBannerContainer {

    private final String HOME_BANNER_DIV_ELEMENT_CSSSELECTOR = "div.about-carousel-block div.slick-slide:not(.slick-cloned)";
    private final String SLICK_DOTS_BOTTOMS_CSSSELECTOR = "div.about-carousel-block  ul.slick-dots.slick-dots-bottom li";
    //
    protected WebDriver driver;
    //
    private List<HomeBannerComponent> homeBannerComponents;
    private List<WebElement> slickDotsBottoms;

    public HomeBannerContainer(WebDriver driver) {
        this.driver = driver;
        initElements();
    }

    private void initElements() {
        // init elements
        homeBannerComponents = new ArrayList<>();
        for (WebElement current : driver.findElements(By.cssSelector(HOME_BANNER_DIV_ELEMENT_CSSSELECTOR))) {
            homeBannerComponents.add(new HomeBannerComponent(current));
        }
        slickDotsBottoms = new ArrayList<>();
        for (WebElement current : driver.findElements(By.cssSelector(SLICK_DOTS_BOTTOMS_CSSSELECTOR))) {
            slickDotsBottoms.add(current);
        }
    }


    // Page Object

    /*
    // productComponents
    public List<ProductComponent> getProductComponents() {
        return productComponents;
    }

    // Functional

    public int getProductComponentsCount()
    {
        return getProductComponents().size();
    }

    public List<String> getProductComponentNames()
    {
        List<String> productComponentNames = new ArrayList<>();
        for (ProductComponent current : getProductComponents())
        {
            productComponentNames.add(current.getNameText());
        }
        return productComponentNames;
    }

    public ProductComponent getProductComponentByName(String productName)
    {
        ProductComponent result = null;
        for (ProductComponent current : getProductComponents())
        {
            if (current.getNameText().toLowerCase()
                    .equals(productName.toLowerCase()))
            {
                result = current;
                break;
            }
        }
        if (result == null)
        {
            // TODO Develop Custom Exception
            // Use String.format()
            throw new RuntimeException("ProductName: " + productName + " not Found.");
        }
        return result;
    }

    // TODO Move to Product
    public String getProductComponentPriceByName(String productName)
    //public String getProductComponentPriceByName(Product productName)
    {
        return getProductComponentByName(productName).getPriceText();
        //return getProductComponentByName(productName.getName()).getPriceText();
    }

    // TODO Move to Product
    public String getProductComponentDescriptionByName(String productName)
    {
        return getProductComponentByName(productName).getPartialDescriptionText();
    }

    // TODO Move to Product
    public void clickProductComponentAddToCartButtonByName(String productName)
    {
        getProductComponentByName(productName).clickAddToCartButton();
    }

    // TODO Move to Product
    public void clickProductComponentAddToWishButtonByName(String productName)
    {
        getProductComponentByName(productName).clickAddToWishButton();
    }
*/

    /*-
    public String getProductComponentPriceByProduct(Product product)
    {
        return getProductComponentPriceByName(product.getName());
    }

    public String getProductComponentDescriptionByProduct(Product product)
    {
        return getProductComponentDescriptionByName(product.getName());
    }
    */

    // Business Logic

    /*-
    public ProductComponent getProductComponentByName(Product product)
    {
        return getProductComponentByName(product.getName());
    }
    */

}
