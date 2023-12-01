import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import ClubsPage from "../PO/ClubsPage";
import BasePage from "../PO/BasePage";
import {SIMPLE_SEARCH_PARAMETERS} from "../constants/searchQueries.constants";
import {CITIES} from "../constants/general.constants";

let apiservice, clubspage, basepage;

test.describe.configure({
    mode: "parallel"
})

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        clubspage = new ClubsPage(page);
        await apiservice.apiLoginAs('admin');
        await clubspage.gotoClubsPage();
    })

    test("Verify that clubs received in the result of the simple search contain search query (search by club name/description)", async ({ page }) => {
        //search by the club name and verification
        await clubspage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.clubNameSearchQuery);
        await clubspage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.clubNameSearchQuery);

        //change the search (by the club description) and verify that the results are correct
        await clubspage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.descriptionSearchQuery);
        await clubspage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.descriptionSearchQuery);
    });

    test("Verify that clubs received in the result of the simple search contain search query (search by category name)", async ({ page }) => {
        //search by the club category and verification/different city
        basepage = new BasePage(page);
        await basepage.selectCityInNavBar(CITIES.kharkiv)
        await clubspage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.categorySearchQuery);
        await clubspage.verifyClubCardsContainText(SIMPLE_SEARCH_PARAMETERS.categorySearchQuery);
    });

    test("Verify that respective message will be shown when there are no results", async ({ page }) => {
        await clubspage.simpleSearchByQuery(SIMPLE_SEARCH_PARAMETERS.incorrectSearchQuery);
        await clubspage.verifyElementVisibility(clubspage.noResultsMessage);
    });

    test("Verify that after advanced search toggle, cards are sorted in alphabetical order", async({page})=>{
        basepage = new BasePage(page);
        await clubspage.toggleAdvancedSearch();
        await clubspage.verifyClubsSortedByTitlesAsc();
    })


    test.afterEach(async({page})=>{
        await page.close();
    })
