import { test} from "@playwright/test";
import ApiService from "../services/ApiService";
import ClubsPage from "../PO/ClubsPage";
import BasePage from "../PO/BasePage";
import {SIMPLE_SEARCH_PARAMETERS} from "../constants/searchQueries.constants";
import {CITIES} from "../constants/general.constants";

let apiService, clubsPage, basePage;

test.describe.configure({
    mode: "parallel"
})

    test.beforeEach(async({page})=>{
        apiService = new ApiService(page);
        clubsPage = new ClubsPage(page);
        await apiService.apiLoginAs('admin');
        await clubsPage.gotoClubsPage();
    })

    test("Verify that clubs received in the result of the simple search contain search query (search by club name/description)", async ({ page }) => {
        //search by the club name and verification
        await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.clubNameSearchQuery);
        await clubsPage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.clubNameSearchQuery);

        //change the search (by the club description) and verify that the results are correct
        await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.descriptionSearchQuery);
        await clubsPage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.descriptionSearchQuery);
    });

    test("Verify that clubs received in the result of the simple search contain search query (search by category name)", async ({ page }) => {
        //search by the club category and verification/different city
        basePage = new BasePage(page);
        await basePage.selectCityInNavBar(CITIES.kharkiv)
        await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.categorySearchQuery);
        await clubsPage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.categorySearchQuery);
    });

    test("Verify that respective message will be shown when there are no results", async ({ page }) => {
        await clubsPage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.incorrectSearchQuery);
        await clubsPage.verifyElementVisibility(clubsPage.noResultsMessage);
    });

    test("Verify that after advanced search toggle, cards are sorted in alphabetical order", async({page})=>{
        basePage = new BasePage(page);
        await clubsPage.toggleAdvancedSearch();
        await clubsPage.verifyClubsSortedByTitlesAsc();
    })


    test.afterEach(async({page})=>{
        await page.close();
    })
