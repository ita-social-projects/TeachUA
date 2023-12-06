import { CHALLENGES_ADMIN_URL, ADD_CHALLENGE_ADMIN_URL } from "../constants/api.constants";
import { IMAGES_PATH } from "../constants/general.constants";
import BasePage from "./BasePage";

class AddChallengePage extends BasePage {
    constructor(page) {
        super(page);
        this.backToChallengesButton = page.locator('a.back-btn[href="/dev/admin/challenges"]');
        this.viewChallengeButton = page.locator('a[href^="/dev/challenges/"] button');
        this.challengeSequenceNumberField = page.locator('input#sortNumber');
        this.challengeNameField = page.locator('input#name');
        this.challengeTitleField = page.locator('input#title');
        this.challengeDescriptionField = page.locator(".ql-editor");
        this.challengePhotoInput = page.locator('input[type="file"]');
        this.saveButton = page.locator('button.flooded-button[type="submit"]');
        this.challengeAddedSuccessMessage = this.page.locator('div.ant-message-success');
    }

    async gotoAddChallengePage() {
        await this.page.goto(ADD_CHALLENGE_ADMIN_URL);
    }

    async openChallengesPage() {
        await this.backToChallengesButton.click();
        await this.verifyUrl(CHALLENGES_ADMIN_URL);
    }

    async uploadChallengePhoto() {
        await this.challengePhotoInput.setInputFiles(IMAGES_PATH + 'challenge1.jpg');
    }

    async confirmChallengeCreation() {
        await this.saveButton.click();
    }
}

module.exports = AddChallengePage;
