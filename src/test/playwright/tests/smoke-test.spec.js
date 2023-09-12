import { chromium, expect, test} from "@playwright/test";
import HomePage from "../PO/homepage";
import BasePage from "../PO/basepage";
import UserPage from "../PO/userpage";

let homepage, basepage, userpage;

    test.beforeEach(async({page})=>{
        homepage = new HomePage(page);
        await homepage.gotoHomepage();
    })

    test('Verify API login works', async ({page})=>{
        basepage = new BasePage(page);
        homepage = new HomePage(page);
        userpage = new UserPage(page);

        await basepage.apiLoginAs('admin');
        await userpage.gotoUserPage();
        await userpage.verifyTitleVisible(true);
    })

    test('Verify UI login works', async ({page}) =>{
        await homepage.uiLoginAs('admin');
    })

    test.afterEach(async({page})=>{
        page.close();
    })

