import { test} from "@playwright/test";
import HomePage from "../PO/HomePage";
import UserPage from "../PO/UserPage";
import ApiService from "../services/ApiService";

let homepage, apiservice, userpage;

test.describe.configure({
    mode: "parallel"
})

    test.beforeEach(async({page})=>{
        homepage = new HomePage(page);
        await homepage.gotoHomepage();
    })

    test('Verify successful login via API @smoke', async ({page})=>{
        apiservice = new ApiService(page);
        userpage = new UserPage(page);

        await apiservice.apiLoginAs('admin');
        await userpage.gotoUserPage();
        await userpage.verifyTitleIsVisible(true);
    })

    test('Verify successful login via UI @smoke', async () =>{
        await homepage.uiLoginAs('admin');
    })

    test.afterEach(async({page})=>{
       await page.close();
    })
