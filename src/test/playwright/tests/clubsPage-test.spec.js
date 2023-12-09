import { test, chromium } from "@playwright/test";
import ApiService from "../services/ApiService";
import ClubsPage from "../PO/ClubsPage";
import BasePage from "../PO/BasePage";
import { SIMPLE_SEARCH_PARAMETERS } from "../constants/searchQueries.constants";
import { CITIES } from "../constants/general.constants";
import { USER_ROLES } from "../constants/general.constants";

let page, apiService, clubsPage, basePage;

test.describe.configure({
    mode: "parallel",
});

test.beforeAll(async () => {
    const browser = await chromium.launch();
    const context = await browser.newContext();
    page = await context.newPage();
    apiService = new ApiService(page);
    basePage = new BasePage(page);
    clubsPage = new ClubsPage(page);
    await apiService.apiLoginAs(USER_ROLES.admin);
});

test.beforeEach(async () => {
    await clubsPage.gotoClubsPage();
});

test("Verify that clubs received in the result of the simple search contain search query (search by club name/description)", async () => {
    // Search by the club name and verification
    await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.clubNameSearchQuery);
    await clubsPage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.clubNameSearchQuery);

    // Change the search (by the club description) and verify that the results are correct
    await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.descriptionSearchQuery);
    await clubsPage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.descriptionSearchQuery);
});

test("Verify that clubs received in the result of the simple search contain search query (search by category name)", async () => {
    // Search by the club category and verification/different city
    await basePage.selectCityInNavBar(CITIES.kharkiv);
    await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.categorySearchQuery);
    await clubsPage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.categorySearchQuery);
});

test("Verify that respective message will be shown when there are no results", async () => {
    await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.incorrectSearchQuery);
    await clubsPage.verifyElementVisibility(clubsPage.noResultsMessage);
});

test("Verify that after advanced search toggle, cards are sorted in alphabetical order", async () => {
    await clubsPage.toggleAdvancedSearch();
    await clubsPage.verifyClubsSortedByTitlesAsc();
});

test.afterAll(async () => {
    await page.close();
});
