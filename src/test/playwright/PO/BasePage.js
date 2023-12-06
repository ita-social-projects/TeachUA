import { expect } from "@playwright/test";

class BasePage {
    constructor(page) {
        this.page = page;
        this.navBarCityDropdown = page.locator('header > div.right-side-menu div.city');
        this.navBarCityDropdownList = page.locator('body > div:last-child ul');
        this.paginationNextPageButton = page.locator('ul > li[title="Next Page"]');
        this.paginationFirstPage = page.locator('li[title="1"]');
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

    /**
     * Asserts that the given text is contained within another text.
     * @param {string} text - The text to search within.
     * @param {string} searchText - The text to search for.
     */
    async assertTextContains(text, searchText) {
        const doesContain = text.includes(searchText);
        expect(doesContain).toBe(true);
    }

    /**
     * Verifies the visibility of an element on a page.
     * @param {Locator} element - The element to check.
     * @param {boolean} isVisible - Indicates whether the element should be visible or not.
     */
    async verifyElementVisibility(element, isVisible = true) {
        if (!(typeof isVisible === "boolean")) {
            throw new Error("Second paramenter should be boolean");
        }
        isVisible
            ? await expect(element, ' should be visible').toBeVisible()
            : await expect(element, 'should NOT be visible').not.toBeVisible();
    }

    // Navigation Methods

    /**
     * Checks if an element with a specific name is present on a page with pagination.
     * @param {Locator} allElements - All elements to search through.
     * @param {string} name - The name to search for.
     * @returns {boolean} - True if the element is present, false otherwise.
     */
    async isElementWithNamePresent(allElements, name) {
        const elementsTextContents = await allElements.allTextContents();
        if (await elementsTextContents.includes(name)) {
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

    /**
     * Verifies the existence of an element with a specific name.
     * @param {Locator} allElements - All elements to search through.
     * @param {string} name - The name to search for.
     * @param {boolean} doesExist - Indicates whether the element should exist or not.
     */
    async verifyElementExistance(allElements, name, doesExist = true) {
        // Navigates to the first page, if it is not selected.
        if (await this.isItFirstPage()) {
            await this.paginationFirstPage.click();
        }
        const isElementPresent = await this.isElementWithNamePresent(allElements, name);
        doesExist ? expect(isElementPresent).toBe(true) : expect(isElementPresent).toBe(false);
    }

    async verifyUrl(expectedUrl) {
        const currentUrl = await this.page.url();
        expect(currentUrl).toBe(expectedUrl);
    }

    async isNextPageAvailable() {
        return (
            (await this.paginationNextPageButton.isVisible()) &&
            (await this.paginationNextPageButton.getAttribute("aria-disabled")) !== "true"
        );
    }

    /**
     * Checks if the current page is the first page.
     * @returns {boolean} - False if it's the first page, true otherwise.
     */
    async isItFirstPage() {
        return (
            (await this.paginationFirstPage.isVisible()) &&
            !(await this.paginationFirstPage.getAttribute("ant-pagination-item-active"))
        );
    }

    async goToNextPage() {
        await this.paginationNextPageButton.click();
    }

    /**
     * Navigates to the next page if available and executes additional actions on the page.
     * @param {Function} actionsOnThePage - Optional function to perform additional actions on the page.
     *                                      Defaults to an empty async function.
     */
    async goToNextPageIfAvailabe(actionsOnThePage = async () => {}) {
        if (await this.isNextPageAvailable()) {
            await this.goToNextPage();
            await actionsOnThePage();
        }
    }

    /**
     * Combines visibility and text checks for an element.
     * @param {Locator} element - The element to check.
     * @param {boolean} isVisible - Indicates whether the element should be visible or not.
     * @param {string} text - The expected text.
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

    /**
     * Verifies that a tooltip appears on hover with the specified message.
     * @param {Locator} selector - The element to hover over.
     * @param {string} message - The expected tooltip message.
     */
    async verifyTooltipAppearsOnHover(selector, message) {
        await selector.hover();
        const tooltip = this.page.locator("div.ant-tooltip-inner").filter({ hasText: `${message}` });
        await this.verifyElementVisibility(tooltip, true);
    }
}

module.exports = BasePage;
