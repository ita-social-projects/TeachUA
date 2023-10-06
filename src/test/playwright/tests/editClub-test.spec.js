import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import AddClubPage from "../PO/AddClubPage";
import UserPage from "../PO/UserPage";
import HomePage from "../PO/HomePage";
import {clubCategories, editLocationDetails, editClubDetails, editClubContactDetails, newClubCorrectDetails} from "../constants/clubInformation.constants";
import {createClubRequest } from "../constants/api.constants";

let apiservice, addclubpage, homepage, userpage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        addclubpage = new AddClubPage(page);
        userpage = new UserPage(page);
        homepage = new HomePage(page);

        await apiservice.apiLoginAs('admin');
        await homepage.gotoHomepage();
        await userpage.gotoUserPage();
    })

    test("Verify that a new club can be succesfully added", async ({ page }) => {
        
        //await userpage.openClubEditMode(clubCreation.body.name);
        //await userpage.openClubEditModeByTitle('Test Automation club 4');
        await userpage.openClubEditModeByTitle('Test Automation club');
        //await userpage.removeClubByTitle('Test Automation club');
        //await userpage.expectClubToExist('Test Automation club 455');
        //await userpage.openClubEditMode('Test Automation club 4');
        // await addclubpage.proceedToNextStep();
        // await addclubpage.openAddLocationWindow();
        // await addclubpage.closeAddLocationWindow();
    });

    test.afterEach(async ({ page }) => {
        //await apiservice.deleteClubByTitle(clubCreation.body.name);
        //await page.close();
    });
