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
        this.clubsNames = page.locator("div.title");
        this.clubsCategories = page.locator("div.club-tags-box span.name");
        this.clubsDescriptions = page.locator("p.description");
        this.clubsANDcategories = page.locator("span.and");
        this.clubDetailsCategories = page.locator("div.tags.categories span.name");
        this.clubDetailsCloseButton = page.locator('button[aria-label="Close"]');
        this.paginationNextPageButton = page.locator('ul > li[title="Next Page"]');
        this.paginationFirstPageButton = page.locator('ul > li[title="1"]');
        this.advancedSearchButton = page.locator('span[title="Розширений пошук"]');
        this.firstCardTitle = page.locator("div.content-clubs-list > div:first-child div.title");
    }

    async gotoClubsPage() {
        await this.page.goto(clubsUrl);
    }

    async toggleAdvancedSearch() {
        await this.advancedSearchButton.click();
        await this.page.waitForTimeout(500);
    }

    async getAllClubsTitles() {
        let allClubsTitles = [];
        while (true) {
            const pageTitlesText = await this.page.locator("div.title").allTextContents();
            allClubsTitles = allClubsTitles.concat(pageTitlesText);
            console.log(pageTitlesText);
            if (await this.isNextPageAvailable()) {
                await this.goToNextPage();
            } else {
                break;
            }
        }
        return allClubsTitles;
    }

    async verifyClubsSortedByTitlesAsc() {
        const originalClubsTitles = await this.getAllClubsTitles();
        const sortedClubsTitles = await this.sortElementsAsc(originalClubsTitles);
        console.log(originalClubsTitles);
        console.log(sortedClubsTitles);
        expect(originalClubsTitles).toMatchObject(sortedClubsTitles);
    }

    async isNextPageAvailable() {
        return (
            (await this.paginationNextPageButton.isVisible()) &&
            (await this.paginationNextPageButton.getAttribute("aria-disabled")) !== "true"
        );
    }

    async goToNextPage() {
        await this.paginationNextPageButton.click();
        //waiting for cards to update on a new page
        await this.page.waitForTimeout(500);
    }

    async verifyTitleToHaveText(text) {
        await this.expectElementToHaveText(this.clubsPageTitle, text);
    }

    async simpleSearchByQuery(query) {
        await this.searchField.clear();
        await this.searchField.fill(query);
        await this.searchButton.click();
    }

    //this method navigates to the next page calls next method if provided
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
            const cardANDcategory = await card.locator(this.clubsANDcategories).textContent();
            if (cardANDcategory.length > 0) {
                await this.openCardDetailsPopUp(card);
                const cardCategories = (await this.clubDetailsCategories.allTextContents()).join("; ").toLowerCase();
                await this.closeCardDetailsPopUp();
                card = ((await card.textContent()) + cardCategories).toLowerCase();
            } else {
                card = (await card.textContent()).toLowerCase();
            }
            await expect(card.includes(text)).toBe(true);
        }
        console.log(`Page of ${text} search query is done-----------------------------------------`);
        await this.goToNextPageIfAvailabe(async()=>{
            await this.verifyClubCardsContainText(text);
        });
    }
}

module.exports = ClubsPage;