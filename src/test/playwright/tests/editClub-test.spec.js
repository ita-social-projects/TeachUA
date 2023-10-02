import { chromium, expect, test} from "@playwright/test";
import ApiService from "../services/apiService";
import AddClubPage from "../PO/AddClubPage";
import UserPage from "../PO/UserPage";
import HomePage from "../PO/HomePage";
import {clubCategories, newLocationCorrectDetails, newClubCorrectContactDetails, newClubIncorrectContactDetails, newClubCorrectDetails, newClubIncorrectDetails, newLocationIncorrectDetails} from "../constants/clubInformation.constants";
import {addClubValidationErrorMessages,addLocationValidationErrorMessages,clubAlreadyExistMessage, successClubCreationMessage,noLocationClubOnlineMessage} from "../constants/messages.constants";

let apiservice, addclubpage, homepage, userpage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        addclubpage = new AddClubPage(page);
        userpage = new UserPage(page);
        homepage = new HomePage(page);

        await apiservice.apiLoginAs('admin');
        await homepage.gotoHomepage();
    })

    test("Verify that a new club can be succesfully added", async ({ page }) => {
        await apiservice.createNewClub();
        await userpage.gotoUserPage();
    });

    test.afterEach(async ({ page }) => {
        await page.close();
    });
