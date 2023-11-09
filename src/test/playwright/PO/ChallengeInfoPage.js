
import BasePage from "./BasePage";

class ChallengeInfoPage extends BasePage {
    constructor(page) {
        super(page);
        this.challengePageTitle = page.locator("h1.ant-typography");
        this.challengeSortNumber = page.locator("input#sortNumber");
        this.challengeStatus = page.locator("button#isActive");
        this.challengeName = page.locator("input#name");
        this.challengeTitle = page.locator("input#title");
        this.challengeDescription = page.locator("div.ql-editor");
        this.saveButton = page.locator('button.flooded-button[type="submit"]');

        this.challengeTasksNames = page.locator("td:nth-child(2)");
    }

}

module.exports = ChallengeInfoPage;