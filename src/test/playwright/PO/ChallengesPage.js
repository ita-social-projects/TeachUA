import { challengesAdminUrl, addChallengeAdminUrl, tasksAdminUrl } from "../constants/api.constants";
import BasePage from "./BasePage";

class ChallengesPage extends BasePage {
    constructor(page) {
        super(page);
        this.challengesPageTitle = page.getByRole("heading", { name: "Челенджі" });
        this.addChallengeButton = page.locator("button.add-btn");
        this.openTasksButton = page.locator("a.back-btn button");
        this.addTaskButton = page.locator("a[href='/dev/admin/addTask']");
        this.allChallengesSequenceNumbers = page.locator("tbody td:nth-child(2)");
        this.firstChallenge = page.locator("tr:first-child td.ant-table-cell:first-child");
        this.editChallengeButtons = page.locator("span.table-action:nth-child(1)");
        this.editConfirmButton = page.locator("span.table-action").filter({ hasText: "Зберегти" });
        this.editCancelButton = page.locator("span.table-action").filter({ hasText: "Відмінити" });

        this.deleteChallengeButtons = page.locator("span.table-action:nth-child(2)");
        //insecure locator below (each time 'delete' button was clicked, it created a new instance). No unique locator available
        this.popUpYes = page.locator("button.popConfirm-ok-button");
        this.popUpNo = page.locator("popConfirm-cancel-button");

        this.challengeSequenceNumberField = page.locator("input#sortNumber");
        this.challengeNameField = page.locator("input#name");
        this.challengeTitleField = page.locator("input#title");

        this.actionSuccessMessage = page.locator("div.ant-message-success");
    }

    async gotoChallengesPage() {
        await this.page.goto(challengesAdminUrl);
    }

    async openAddChallengePage() {
        await this.addChallengeButton.click();
        await this.verifyUrl(addChallengeAdminUrl);
    }

    async openTasksPage() {
        await this.openTasksButton.click();
        await this.verifyUrl(tasksAdminUrl);
    }
        

    async openChallengeInfoPage(challengeSortNumber) {
        await this.verifyElementVisibility(this.firstChallenge);

        const challenge = await this.allChallengesSequenceNumbers.filter({
            has: this.page.getByText(challengeSortNumber, { exact: true }),
        });
        if (await challenge.isVisible()) {
            await challenge.click();
            return;
        } else if (await this.isNextPageAvailable()) {
            await this.goToNextPage();
            await this.openChallengeInfoPage(challengeSortNumber);
        } else {
            throw new Error("No such challenge exist");
        }
    }

    async selectChallengeManageOption(challengeSortNumber, option) {
        const challenge = await this.allChallengesSequenceNumbers.filter({
            has: this.page.getByText(challengeSortNumber, { exact: true }),
        });
        if (await challenge.isVisible()) {
            const row = await this.page.getByRole("row", { name: challengeSortNumber });
            await row.locator(option).click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            await this.selectChallengeManageOption(challengeSortNumber, option);
        } else {
            // If the club is not found and there are no more pages, throw an error
            throw new Error("No such challenge exists");
        }
    }

    async turnOnEditChallenge(challengeSortNumber) {
        await this.selectChallengeManageOption(challengeSortNumber + "", this.editChallengeButtons);
        await this.verifyElementVisibility(this.challengeSequenceNumberField);
    }

    async confirmEditChallenge() {
        await this.editConfirmButton.click();
        await this.popUpYes.click();
        await this.verifyElementVisibility(this.editConfirmButton, false);
        await this.verifyElementVisibility(this.editCancelButton, false);
    }

    async deleteChallenge(challengeSortNumber) {
        await this.selectChallengeManageOption(challengeSortNumber + "", this.deleteChallengeButtons);
        await this.popUpYes.click();
        const challenge = await this.page.getByText(challengeSortNumber, { exact: true })
        await this.verifyElementVisibility(challenge, false);
    }
}

module.exports = ChallengesPage;
