import { expect, test} from "@playwright/test";
import HomePage from "../PO/homepage";


let homepage;

    test.beforeEach(async({page})=>{
        homepage = new HomePage(page);


        const response = await fetch('http://localhost:8080/dev/api/signin', {
            method: 'POST',
            body: JSON.stringify({email: 'admin@gmail.com', password: 'admin'}),
            headers: { 'Content-Type': 'application/json' },
        })
        let jsonResponse = await response.json();
        console.log(jsonResponse);
        
        await page.setExtraHTTPHeaders({Authorization: 'Bearer ' + jsonResponse.accessToken})

        await homepage.gotoHomepage();
        // await page.setCookie({
        //     name: 'Authorization', // Set the cookie name to match your application's expected token
        //     value: "Bearer " + authToken,
        //     domain: 'http://localhost:3000/dev/',
        //     path: '/',
        //   });

        //await homepage.gotoHomepage();
    })

    test.only('Go to admin page', async ({page})=>{
        await page.goto('http://localhost:3000/dev/admin/change-club-owner');
        console.log('Opened adin page')
    })


    test('Open homepage and verify that cities are present in the dropdown menu @smoke', async ({page}) =>{
        await homepage.clickOnDropdown();
    })

    test('Open homepage and verify that you can log in', async ({page}) =>{
        await homepage.loginAs('admin');
    })

    test.afterEach(async({page})=>{
        await page.close();
    })

