import { challengesAdminUrl, ADD_CHALLENGE_ADMIN_URL } from "../constants/api.constants";
import { IMAGES_PATH } from "../constants/general.constants";
import BasePage from "./BasePage";

class AddTaskPage extends BasePage {
    constructor(page) {
        super(page);
        this.backToTasksButton = page.locator('a.back-btn[href="/dev/admin/tasks"]');
        this.viewTaskButton = page.locator('div.add-form a:nth-child(2)');
        this.taskDateStartField = page.locator('input#startDate');
        this.taskPhotoInput = page.locator('input[type="file"]');
        this.taskNameField = page.locator('input#name');
        this.taskTitleField = page.locator("div.ant-form-item:nth-child(4) .ql-editor");
        this.taskDescriptionField = page.locator("div.ant-form-item:nth-child(5) .ql-editor");
        this.taskChallengeField = page.locator("input#challengeId");
        this.saveButton = page.locator('button.flooded-button[type="submit"]');

        this.taskAddedSuccessMessage = this.page.locator("div.ant-message-success");
        
    }

    async gotoAddChallengePage() {
        await this.page.goto(ADD_CHALLENGE_ADMIN_URL);
    }

    async openChallengesPage() {
        await this.backToChallengesButton.click();
        await this.verifyUrl(challengesAdminUrl)
    }

    async selectDate(date){
        await this.fillInputField(this.taskDateStartField, date);
        await this.taskDateStartField.press('Enter');
    }

    async uploadTaskPhoto() {
        await this.taskPhotoInput.setInputFiles(IMAGES_PATH + 'task1.jpg');
    }

    async confirmTaskCreation(){
        await this.saveButton.click();
    }

    async selectChallenge(challengeName){
        await this.taskChallengeField.click();
        await this.page.locator('div.ant-select-item-option-content').filter({hasText: challengeName}).click();
    }
}

module.exports = AddTaskPage;
