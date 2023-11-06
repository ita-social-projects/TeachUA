import { expect } from "@playwright/test";
import { challengesAdminUrl, addChallengeAdminUrl, tasksAdminUrl } from "../constants/api.constants";
import { imagesPath } from "../constants/general.constants";
import BasePage from "./BasePage";

class AddChallengePage extends BasePage {
    constructor(page) {
        super(page);
        this.backToChallengesButton = page.locator('a.back-btn[href="/dev/admin/challenges"]');
        this.viewChallengeButton = page.locator('div.add-form a:nth-child(2)');
        this.challengeSequenceNumberField = page.locator('input#sortNumber');
        this.challengeNameField = page.locator('input#name');
        this.challengeTitleField = page.locator('input#title');
        this.challengeDescriptionField = page.locator(".ql-editor");
        this.challengePhotoInput = page.locator('input[type="file"]');
        this.saveButton = page.locator('button.flooded-button[type="submit"]');

        this.challengeAddedSuccessMessage = this.page.locator("div.ant-message-success");
        
    }

    async gotoAddChallengePage() {
        await this.page.goto(addChallengeAdminUrl);
    }

    async openChallengesPage() {
        await this.backToChallengesButton.click();
        await this.verifyUrl(challengesAdminUrl)
    }

    async uploadChallengePhoto() {
        await this.challengePhotoInput.setInputFiles(imagesPath + 'challenge1.jpg');
    }

    async confirmChallengeCreation(){
        await this.saveButton.click();
    }
}

module.exports = AddChallengePage;
