import { expect} from "@playwright/test";

class BasePage {
    constructor(page) {
        this.page = page;
        this.navBarCityDropdown = page.locator("header > div.right-side-menu div.city");
        this.navBarCityDropdownList = page.locator("body > div:last-child ul");
        this.paginationNextPageButton = page.locator('ul > li[title="Next Page"]');
    }

    async expectElementToHaveText(element, text) {
        await expect(element).toHaveText(text);
    }

    async expectElementToContainText(element, text) {
        await expect(element).toContainText(text);
    }

    async expectElementToNotContainText(element, text) {
        await expect(element).not.toContainText(text);
    }

    async assertTextContains(text, searchText) {
        const doesContain = text.includes(searchText);
        expect(doesContain).toBe(true);
    }

    async verifyElementVisibility(element, isVisible = true) {
        if (!(typeof isVisible === "boolean")) {
            throw new Error("Second paramenter should be boolean");
        }
        isVisible ? await expect(element, element + ' should be visible').toBeVisible() : await expect(element, 'should NOT be visible').not.toBeVisible();
    }

    async isElementWithNamePresent(allElements, name){
        await this.verifyElementVisibility(await allElements.nth(0));
        const element = await allElements.allTextContents();
        if (await element.includes(name)) {
            return true;
        } else if (await this.isNextPageAvailable()) {
            // If the element is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            return await this.isElementWithNamePresent(allElements, name);
        } else {
            // If the element is not found and there are no more pages, return false
            return false;
        }
    }

    //use the method above to make assertion
    async verifyElementExistance(allElements, name, doesExist = true) {
        doesExist
            ? expect(await this.isElementWithNamePresent(allElements, name)).toBe(true)
            : expect(await this.isElementWithNamePresent(allElements, name)).toBe(false);
    }

    async verifyUrl(expectedUrl){
        const currentUrl = await this.page.url();
        expect(currentUrl).toBe(expectedUrl);
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

    /*
     *This method can be used to check whether the element is present and, if visible, 
     *has the required text.
     *It combines both checks to reduce code duplication when 
     *verifying error messages' presence or absence.
     */
    async verifyElementVisibilityAndText(element, isVisible = true, text) {
        await this.verifyElementVisibility(element, isVisible);
        if (isVisible === true) {
            await this.expectElementToHaveText(element, text);
        }
    }

    async fillInputField(element, value) {
        await element.waitFor({ timeout: 5000 });
        await element.click();
        await element.clear();
        await element.fill(value);
    }

    async selectCityInNavBar(city) {
        await this.navBarCityDropdown.click();
        await (await this.navBarCityDropdownList.getByText(city)).click();
    }

    async sortElementsAsc([...elements]) {
        return elements.sort();
    }

    async sortElementsDesc([...elements]) {
        return elements.sort((a, b) => b - a);
    }

    async verifyTooltipAppearsOnHover(selector, message) {
        await selector.hover();
        const tooltip = this.page.locator("div.ant-tooltip-inner").filter({ hasText: `${message}` });
        await this.verifyElementVisibility(tooltip, true);
    }
}

module.exports = BasePage;