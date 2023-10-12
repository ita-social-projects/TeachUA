import { expect} from "@playwright/test";
import {signInUrl} from "../constants/api.constants";

class BasePage {
    constructor(page) {
        this.page = page;
        this.navBarCityDropdown = page.locator('header > div.right-side-menu div.city')
        this.navBarCityDropdownList = page.locator('body > div:last-child ul')
        this.paginationNextPageButton = page.locator('ul > li[title="Next Page"]');
    }

    async expectElementToHaveText(element, text) {
        await expect(element).toHaveText(text);
    }

    async expectElementToContainText(element, text) {
        await expect(element).toContainText(text);
    }

    async assertTextContains(text, searchText) {
        const doesContain = text.includes(searchText);
        expect(doesContain).toBe(true);
    }

    async verifyElementVisibility(element, isVisible = true) {
        if(!(typeof isVisible === 'boolean')){
          throw new Error('Second paramenter should be boolean');
        }
        if (isVisible === true) {
            await expect(element).toBeVisible();
        } else if (isVisible === false) {
            await expect(element).not.toBeVisible();
        }
    }

    async isNextPageAvailable() {
        return (
            (await this.paginationNextPageButton.isVisible()) &&
            (await this.paginationNextPageButton.getAttribute("aria-disabled")) !== "true"
        );
    }

    async goToNextPage() {
        await this.paginationNextPageButton.click();
    }

    async goToNextPageIfAvailabe(actionsOnThePage = async () => {}) {
        if (await this.isNextPageAvailable()) {
            await this.goToNextPage();
            await actionsOnThePage();
        }
    }

    //this method can be used to check whether the element is present and has the required text, or not visible
    //I combined it in order to reduce code duplication when verifying error messages presence/absence
    async verifyElementVisibilityAndText(element, isVisible=true, text){
        if(isVisible===true){
            await this.verifyElementVisibility(element, isVisible);
            await this.expectElementToHaveText(element,text);
        } else {
            await this.verifyElementVisibility(element, isVisible);
        }
    }


    async fillInputField(element, value){
        await element.waitFor({timeout: 5000});
        await element.clear();
        await element.fill(value);
    }

    async selectCityInNavBar(city){
        await this.navBarCityDropdown.click();
        await (await this.navBarCityDropdownList.getByText(city)).click();
    }

    async sortElementsAsc([...elements]) {
        return elements.sort();
    }

    async sortElementsDesc([...elements]) {
        return elements.sort((a, b) => b - a);
    }

    async verifyTooltipAppearsOnHover(selector,message){
        await selector.hover();
        const tooltip = this.page.locator('div.ant-tooltip-inner').filter({ hasText: `${message}`});;
        await this.verifyElementVisibility(tooltip, true);
    }
}

module.exports = BasePage;