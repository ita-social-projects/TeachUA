import { expect, test} from "@playwright/test";
import HomePage from "../PO/homepage";
import BasePage from "../PO/basepage";

let homepage, basepage;

    test.beforeEach(async({page})=>{
        homepage = new HomePage(page);
        basepage = new BasePage(page);

        basepage.apiLoginAs('admin');

        await homepage.gotoHomepage();
        //await homepage.gotoHomepage();
    })

    test('Go to admin page', async ({page})=>{
        await page.goto('http://localhost:3000/dev/logs');
        console.log('Opened adin page')
    })


    test('Open homepage and verify that cities are present in the dropdown menu @smoke', async ({page}) =>{
        await homepage.clickOnDropdown();
    })

    test('Open homepage and verify that you can log in', async ({page}) =>{
        await homepage.uiLoginAs('admin');
    })

    test.afterEach(async({page})=>{
        await page.close();
    })

