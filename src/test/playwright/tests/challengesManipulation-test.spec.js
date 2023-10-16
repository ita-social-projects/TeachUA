import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import ChallengesPage from "../PO/ChallengesPage";
import BasePage from "../PO/BasePage";

let apiservice, challengespage, basepage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        challengespage = new ChallengesPage(page);
        await apiservice.apiLoginAs('admin');
        await challengespage.gotoChallengesPage();
    })

    test("Verify that a new challenge can be successfully created", async ({ page }) => {
        await challengespage.openTasksPage();
    });

    test.afterEach(async({page})=>{
        //page.close();
    })
