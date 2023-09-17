import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import GroupsPage from "../PO/clubsPage";
import BasePage from "../PO/basePage";
import {simpleSearchParameters} from "../constants/searchQueries.constants";
import {cities} from "../constants/general.constants";

let apiservice, clubspage, basepage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        clubspage = new GroupsPage(page);
        await apiservice.apiLoginAs('admin');
        await clubspage.gotoClubsPage();
    })

    test("Verify that clubs received in the result of the simple search contain search query (search by club name)", async ({ page }) => {
        //search by the club name and verification
        await clubspage.simpleSearchByQuery(simpleSearchParameters.CLUBNAME_SEARCH_QUERY);
        await clubspage.verifyClubCardsContainText(simpleSearchParameters.CLUBNAME_SEARCH_QUERY);
    });

    test("Verify that clubs received in the result of the simple search contain search query (search by category name)", async ({ page }) => {
        //search by the club category and verification
        basepage = new BasePage(page);
        await basepage.selectCityInNavBar(cities.DNIPRO)
        await clubspage.simpleSearchByQuery(simpleSearchParameters.CATEGORY_SEARCH_QUERY);
        await clubspage.verifyClubCardsContainText(simpleSearchParameters.CATEGORY_SEARCH_QUERY);
    });

    test("Verify that clubs received in the result of the simple search contain search query (search by club description)", async ({ page }) => {
        //search by the club description and verification
        await clubspage.simpleSearchByQuery(simpleSearchParameters.DESCRIPTION_SEARCH_QUERY);
        await clubspage.verifyClubCardsContainText(simpleSearchParameters.DESCRIPTION_SEARCH_QUERY);
    });

    // test("test", async({page})=>{
        
    // })


    test.afterEach(async({page})=>{
        //page.close();
    })
