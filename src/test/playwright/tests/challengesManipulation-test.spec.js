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
        try {
            await apiservice.deleteChallengeByName(newChallengeCorrectDetails.NAME)
        } catch (e) {
            console.error(e);
        }
        await challengespage.openAddChallengePage();
        await addchallengepage.fillInputField(addchallengepage.challengeSequenceNumberField, newChallengeCorrectDetails.SEQUENCE_NUMBER)
        await addchallengepage.fillInputField(addchallengepage.challengeNameField, newChallengeCorrectDetails.NAME)
        await addchallengepage.fillInputField(addchallengepage.challengeTitleField, newChallengeCorrectDetails.TITLE)
        await addchallengepage.fillInputField(addchallengepage.challengeDescriptionField, newChallengeCorrectDetails.DESCRIPTION)
        await addchallengepage.uploadChallengePhoto();
        await addchallengepage.confirmChallengeCreation();

        await addchallengepage.verifyElementVisibility(addchallengepage.challengeAddedSuccessMessage);
        await addchallengepage.openChallengesPage();
        await addchallengepage.verifyElementExistance(challengespage.allChallengesSequenceNumbers, newChallengeCorrectDetails.SEQUENCE_NUMBER)

    });

    test("Verify that a new task can be added to the existing challenge", async({page})=>{
        await apiservice.createNewChallenge();
    })


    test.afterEach(async({page})=>{
        //page.close();
    })
