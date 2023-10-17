import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import ChallengesPage from "../PO/ChallengesPage";
import AddChallengePage from "../PO/AddChallengePage";
import BasePage from "../PO/BasePage";
import {newChallengeCorrectDetails} from "../constants/challengeAndTaskInformation.constants";


let apiservice, challengespage, basepage, addchallengepage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        challengespage = new ChallengesPage(page);
        await apiservice.apiLoginAs('admin');
        await challengespage.gotoChallengesPage();
    })

    test("Verify that a new challenge can be successfully created", async ({ page }) => {
        addchallengepage = new AddChallengePage(page);
        await challengespage.openAddChallengePage();
        await addchallengepage.fillInputField(addchallengepage.challengeSequenceNumberField, newChallengeCorrectDetails.SEQUENCE_NUMBER)
        await addchallengepage.fillInputField(addchallengepage.challengeNameField, newChallengeCorrectDetails.NAME)
        await addchallengepage.fillInputField(addchallengepage.challengeTitleField, newChallengeCorrectDetails.TITLE)
        await addchallengepage.fillInputField(addchallengepage.challengeDescriptionField, newChallengeCorrectDetails.DESCRIPTION)
        await addchallengepage.uploadChallengePhoto();
        await addchallengepage.confirmChallengeCreation();

        await addchallengepage.verifyElementVisibility(addchallengepage.challengeAddedSuccessMessage);
    });

    test("test", async ({ page }) => {
        await apiservice.deleteChallengeByName("NEW UPDATE NAME")
    });

    test.afterEach(async({page})=>{
        //page.close();
    })
