import { expect} from "@playwright/test";
import {CLUBS_URL} from "../constants/api.constants";
import BasePage from "./BasePage";

class ClubsPage extends BasePage {
    constructor(page) {
        super(page);
        this.clubsPageTitle = page.locator(".city-name");
        this.searchField = page.locator('div.search-container input[type="search"]');
        this.searchButton = page.locator('span[aria-label="search"]');
        this.cards = page.locator("div.ant-card");
        this.clubsNames = page.locator("div.title");
        this.clubsCategories = page.locator("div.club-tags-box span.name");
        this.clubsDescriptions = page.locator("p.description");
        this.clubsANDcategories = page.locator("span.and");
        this.clubDetailsCategories = page.locator("div.tags.categories span.name");
        this.clubDetailsCloseButton = page.locator('button[aria-label="Close"]');
        this.paginationNextPageButton = page.locator('ul > li[title="Next Page"]');
        this.paginationFirstPageButton = page.locator('ul > li[title="1"]');
        this.advancedSearchButton = page.locator('span[title="Розширений пошук"]');
        this.firstCardTitle = page.locator("div.content-clubs-list > div:first-child div.name");

        this.noResultsMessage = page.locator('div.clubs-not-found');
    }

    async gotoClubsPage() {
        await this.page.goto(CLUBS_URL);
    }

    // Click the advanced search button and wait for a short timeout for clubs to update
    async toggleAdvancedSearch() {
        await this.advancedSearchButton.click();
        await this.page.waitForTimeout(500);
    }

    /*
     * Method to accumulate titles of all club cards
     * Iterates through pages of club cards, accumulating their titles
     * Uses the firstTitleLocator to ensure that previous clubs have disappeared
     * from the page before adding new titles to the array
     */
    async getAllClubsTitles() {
        let allClubsTitles = [];
        while (true) {
            try {
                const firstTitle = await this.firstCardTitle.textContent();
                const firstTitleLocator = await this.page.getByRole("div", { name: firstTitle });
                const pageTitlesText = await this.clubsNames.allTextContents();
                allClubsTitles = allClubsTitles.concat(pageTitlesText);
                console.log(pageTitlesText);
                if (await this.isNextPageAvailable()) {
                    await this.goToNextPage();
                    await firstTitleLocator.waitFor({ state: "hidden", timeout: 10000 });
                } else {
                    break;
                }
            } catch (e) {
                console.error("Error " + e);
                break;
            }
        }
        return allClubsTitles;
    }

    /*
     * Asserts that accumulated club titles are sorted in ascending order
     */
    async verifyClubsSortedByTitlesAsc() {
        const originalClubsTitles = await this.getAllClubsTitles();
        const sortedClubsTitles = await this.sortElementsAsc(originalClubsTitles);
        expect(originalClubsTitles).toMatchObject(sortedClubsTitles);
    }

    // Check if the next page button is visible and enabled
    async isNextPageAvailable() {
        return (
            (await this.paginationNextPageButton.isVisible()) &&
            (await this.paginationNextPageButton.getAttribute("aria-disabled")) !== "true"
        );
    }

    async goToNextPage() {
        await this.paginationNextPageButton.click();
    }

    async simpleSearchByQuery(query) {
        const firstTitle = await this.firstCardTitle.textContent();
        const firstTitleLocator = await this.page.getByRole("div", { name: firstTitle });
        await this.searchField.clear();
        await this.searchField.fill(query);
        await this.searchButton.click();
        await firstTitleLocator.waitFor({ state: "detached", timeout: 10000 });
    }

    /*
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

    async openCardDetailsPopUp(card) {
        await card.locator(this.clubsNames).click();
    }

    async closeCardDetailsPopUp() {
        await this.clubDetailsCloseButton.click();
    }

    async goToFirstPage() {
        await this.paginationFirstPageButton.click();
    }

    /*
     * Method to verify that club cards contain the specified text
     * Iterates through club cards, comparing their text with the search query
     * Opens card details to check for additional categories, if any
     * Utilizes recursion to iterate through all pages
     */
    async verifyClubCardsContainText(text) {
        text = text.toLowerCase();
        const cards = await this.cards.all();
        if (await cards.length === 0) {
            throw new Error("There are no result for this search query!");
        }
        for (let card of cards) {
            const cardANDcategory = await card.locator(this.clubsANDcategories).textContent();
            if (cardANDcategory.length > 0) {
                await this.openCardDetailsPopUp(card);
                const cardCategories = (await this.clubDetailsCategories.allTextContents()).join("; ").toLowerCase();
                await this.closeCardDetailsPopUp();
                card = ((await card.textContent()) + cardCategories).toLowerCase();
            } else {
                card = (await card.textContent()).toLowerCase();
            }
            await this.assertTextContains(card, text);
        }
        await this.goToNextPageIfAvailabe(async () => {
            await this.verifyClubCardsContainText(text);
        });
    }
}

module.exports = ClubsPage;