import { expect} from "@playwright/test";
import {clubsUrl} from "../constants/api.constants";
import BasePage from "./basePage";

class ClubsPage extends BasePage {
    constructor(page) {
        super(page);
        this.clubsPageTitle = page.locator(".city-name");
        this.searchField = page.locator('div.search-container input[type="search"]');
        this.searchButton = page.locator('span[aria-label="search"]');
        this.cards = page.locator("div.ant-card");
        this.clubsNamesLocator = page.locator("div.title");
        this.clubsCategoriesLocator = page.locator("div.club-tags-box span.name");
        this.clubsDescriptionsLocator = page.locator("p.description");
        this.clubsANDcategoriesLocator = page.locator("span.and");
        this.clubDetailsCategoriesLocator = page.locator("div.tags.categories span.name");
        this.clubDetailsCloseButton = page.locator('button[aria-label="Close"]');
        this.paginationNextPageButton = page.locator('ul > li[title="Next Page"]');
        this.paginationFirstPageButton = page.locator('ul > li[title="1"]');
    }

    async gotoClubsPage() {
        await this.page.goto(clubsUrl);
    }

    async verifyTitleToHaveText(text) {
        await this.expectElementToHaveText(this.clubsPageTitle, text);
    }

    async simpleSearchByQuery(query) {
        await this.searchField.clear();
        await this.searchField.fill(query);
        await this.searchButton.click();
    }

    async sortElementsByTitle() {
        let titlesArray = await this.clubsNames.allTextContents();
        titlesArray.sort();
    }

    async goToNextPageIfAvailable(actionsOnThePage = async () => {}) {
        if (await this.paginationNextPageButton.isVisible()) {
            if ((await this.paginationNextPageButton.getAttribute("aria-disabled")) != "true") {
                await this.paginationNextPageButton.click();
                await actionsOnThePage();
            }
        }
    }

    async openCardDetailsPopUp(card){
        await card.locator(this.clubsNamesLocator).click();
    }

    async closeCardDetailsPopUp(){
        await this.clubDetailsCloseButton.click();
    }

    async goToFirstPage(){
        await this.paginationFirstPageButton.click();
    }

    async verifyClubCardsContainText(text) {
        //convert search query to lower case so that it can be compared with the card's text
        text = text.toLowerCase();
        const cards = await this.cards.all();
        if (cards.length === 0) {
            throw new Error("There are no result for this search query!");
        }
        //iterate through each of the club card, convert it's text value to lower case
        //and compare it with the search query. If there are additional categories, then the card
        //details will be opened and these additional categories will be added to the card's text value
        for (let card of cards) {
            const cardANDcategory = await card.locator(this.clubsANDcategoriesLocator).textContent();
            if (cardANDcategory.length > 0) {
                await this.openCardDetailsPopUp(card);
                const cardCategories = (await this.clubDetailsCategoriesLocator.allTextContents())
                    .join("; ")
                    .toLowerCase();
                await this.closeCardDetailsPopUp();
                card = ((await card.textContent()) + cardCategories).toLowerCase();
            } else {
                card = (await card.textContent()).toLowerCase();
            }
            await expect(card.includes(text)).toBe(true);
        }
        console.log(`Page of ${text} search query is done-----------------------------------------`)
        await this.goToNextPageIfAvailable(this.verifyClubCardsContainText.bind(this, text));
    }
}

module.exports = ClubsPage;