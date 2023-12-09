import { test, chromium } from "@playwright/test";
import ApiService from "../services/ApiService";
import ChallengesPage from "../PO/ChallengesPage";
import AddChallengePage from "../PO/AddChallengePage";
import ChallengeInfoPage from "../PO/ChallengeInfoPage"
import {
    CREATE_CHALLENGE_REQUEST,
} from "../constants/api.constants";
import {
    NEW_CHALLENGE_CORRECT_DETAILS,
    EDITED_CHALLENGE_CORRECT_DETAILS,
} from "../constants/challengeAndTaskInformation.constants";
import { USER_ROLES } from "../constants/general.constants";

let page, apiService, challengesPage, addChallengePage, challengeInfoPage;

test.beforeAll(async () => {
    const browser = await chromium.launch();
    const context = await browser.newContext();
    page = await context.newPage();
    apiService = new ApiService(page);
    challengeInfoPage = new ChallengeInfoPage(page);
    challengesPage = new ChallengesPage(page);
    addChallengePage = new AddChallengePage(page);

    await apiService.apiLoginAs(USER_ROLES.admin);
    try {
        await apiService.deleteChallengeBySequenceNumber(NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber);
    } catch (err) {
        console.log(err);
    }
});

test.beforeEach(async () => {
    await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
    await challengesPage.gotoChallengesPage();
});

test("Verify that a new challenge can be successfully created", async () => {
    await challengesPage.openAddChallengePage();
    await addChallengePage.fillInputField(
        addChallengePage.challengeSequenceNumberField,
        NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
    );
    await addChallengePage.fillInputField(addChallengePage.challengeNameField, NEW_CHALLENGE_CORRECT_DETAILS.name);
    await addChallengePage.fillInputField(addChallengePage.challengeTitleField, NEW_CHALLENGE_CORRECT_DETAILS.title);
    await addChallengePage.fillInputField(
        addChallengePage.challengeDescriptionField,
        NEW_CHALLENGE_CORRECT_DETAILS.description
    );
    await addChallengePage.uploadChallengePhoto();
    await addChallengePage.confirmChallengeCreation();

    await addChallengePage.verifyElementVisibility(addChallengePage.challengeAddedSuccessMessage);
    await addChallengePage.openChallengesPage();
    await addChallengePage.verifyElementExistance(
        challengesPage.allChallengesSequenceNumbers,
        NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
    );
});

test("Verify that a challenge can be succesfully deleted", async () => {
    await challengesPage.deleteChallenge(CREATE_CHALLENGE_REQUEST.body.sortNumber);
    await challengesPage.verifyElementVisibility(challengesPage.actionSuccessMessage);
    await challengesPage.verifyElementExistance(
        challengesPage.allChallengesSequenceNumbers,
        CREATE_CHALLENGE_REQUEST.body.sortNumber,
        false
    );
});

test("Verify that an existing challenge can be edited on the challenges list page", async () => {
    await apiService.deleteChallengeBySequenceNumber(EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber);
    await challengesPage.gotoChallengesPage();
    await challengesPage.turnOnEditChallenge(CREATE_CHALLENGE_REQUEST.body.sortNumber);
    await challengesPage.fillInputField(
        challengesPage.challengeSequenceNumberField,
        EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
    );
    await challengesPage.fillInputField(challengesPage.challengeNameField, EDITED_CHALLENGE_CORRECT_DETAILS.name);
    await challengesPage.fillInputField(challengesPage.challengeTitleField, EDITED_CHALLENGE_CORRECT_DETAILS.title);
    await challengesPage.confirmEditChallenge();
    await challengesPage.verifyElementExistance(
        challengesPage.allChallengesSequenceNumbers,
        EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber,
        true
    );
});

test("Verify that an existing challenge can be edited", async () => {
    await apiService.deleteChallengeBySequenceNumber(EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber);
    await challengesPage.gotoChallengesPage();
    await challengesPage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
    await addChallengePage.fillInputField(
        addChallengePage.challengeSequenceNumberField,
        EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
    );
    await addChallengePage.fillInputField(addChallengePage.challengeNameField, EDITED_CHALLENGE_CORRECT_DETAILS.name);
    await addChallengePage.fillInputField(addChallengePage.challengeTitleField, EDITED_CHALLENGE_CORRECT_DETAILS.title);
    await addChallengePage.fillInputField(
        addChallengePage.challengeDescriptionField,
        EDITED_CHALLENGE_CORRECT_DETAILS.description
    );
    await addChallengePage.confirmChallengeCreation();
    await addChallengePage.verifyElementVisibility(addChallengePage.challengeAddedSuccessMessage);
    await challengesPage.gotoChallengesPage();
    await challengesPage.verifyElementExistance(
        challengesPage.allChallengesSequenceNumbers,
        EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber,
        true
    );
});

test.afterAll(async () => {
    await page.close();
});
