import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import GroupsPage from "../PO/ClubsPage";
import BasePage from "../PO/BasePage";
import {simpleSearchParameters} from "../constants/searchQueries.constants";
import {cities} from "../constants/general.constants";

let apiservice, clubspage, basepage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        clubspage = new GroupsPage(page);
        await apiservice.apiLoginAs('admin');
        await clubspage.gotoClubsPage();
    })

    test("Verify that clubs received in the result of the simple search contain search query (search by club name/description)", async ({ page }) => {
        //search by the club name and verification
        await clubspage.simpleSearchByQuery(simpleSearchParameters.CLUBNAME_SEARCH_QUERY);
        await clubspage.verifyClubCardsContainText(simpleSearchParameters.CLUBNAME_SEARCH_QUERY);

        //change the search (by the club description) and verify that the results are correct
        await clubspage.simpleSearchByQuery(simpleSearchParameters.DESCRIPTION_SEARCH_QUERY);
        await clubspage.verifyClubCardsContainText(simpleSearchParameters.DESCRIPTION_SEARCH_QUERY);
    });

    test("Verify that clubs received in the result of the simple search contain search query (search by category name)", async ({ page }) => {
        //search by the club category and verification/different city
        basepage = new BasePage(page);
        await basepage.selectCityInNavBar(cities.KHARKIV)
        await clubspage.simpleSearchByQuery(simpleSearchParameters.CATEGORY_SEARCH_QUERY);
        await clubspage.verifyClubCardsContainText(simpleSearchParameters.CATEGORY_SEARCH_QUERY);
    });

    test("Verify that after advanced search toggle, cards are sorted in alphabetical order", async({page})=>{
        basepage = new BasePage(page);
        await clubspage.toggleAdvancedSearch();
        await clubspage.verifyClubsSortedByTitlesAsc();
    })


    test.afterEach(async({page})=>{
        //page.close();
    })
