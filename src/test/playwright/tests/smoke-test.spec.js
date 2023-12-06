import { test} from "@playwright/test";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import ApiService from "../services/ApiService";

let homePage, apiService, userPage;

test.describe.configure({
    mode: "parallel"
})

    test.beforeEach(async({page})=>{
        homePage = new HomePage(page);
        await homePage.gotoHomepage();
    })

    test('Verify successful login via API @smoke', async ({page})=>{
        apiService = new ApiService(page);
        userPage = new UserPage(page);

        await apiService.apiLoginAs('admin');
        await userPage.gotoUserPage();
        await userPage.verifyTitleIsVisible(true);
    })

    test('Verify successful login via UI @smoke', async () =>{
        await homePage.uiLoginAs('admin');
    })

    test.afterEach(async({page})=>{
       await page.close();
    })
