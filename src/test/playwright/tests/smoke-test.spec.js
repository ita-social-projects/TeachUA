import { chromium, expect, test} from "@playwright/test";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import ApiService from "../services/apiService";

let homepage, apiservice, userpage;

    test.beforeEach(async({page})=>{
        homepage = new HomePage(page);
        await homepage.gotoHomepage();
    })

    test('Verify successful login via API', async ({page})=>{
        apiservice = new ApiService(page);
        userpage = new UserPage(page);

        await apiservice.apiLoginAs('admin');
        await userpage.gotoUserPage();
        await userpage.verifyTitleIsVisible(true);
    })

    test('Verify successful login via UI', async () =>{
        await homepage.uiLoginAs('admin');
    })

    test.afterEach(async({page})=>{
        page.close();
    })

