import { CHALLENGES_ADMIN_URL, ADD_CHALLENGE_ADMIN_URL, TASKS_ADMIN_URL } from "../constants/api.constants";
import { CHALLENGES_PAGE } from "../constants/locatorsText.constants";
import BasePage from "./BasePage";

class ChallengesPage extends BasePage {
    constructor(page) {
        super(page);
        this.challengesPageTitle = page.getByRole('heading', { name: CHALLENGES_PAGE.challenges });
        this.addChallengeButton = page.locator('button.add-btn');
        this.openTasksButton = page.locator('a.back-btn button');
        this.addTaskButton = page.locator('a[href="/dev/admin/addTask"]');
        this.allChallengesSequenceNumbers = page.locator(
            `td.ant-table-cell:nth-child(${CHALLENGES_PAGE.sequenceNumberTableColumn})`
        );
        this.firstChallenge = page.locator('tr:first-child td.ant-table-cell:first-child');
        this.editChallengeButtons = page.locator('span.table-action').filter({ hasText: CHALLENGES_PAGE.edit });
        this.editConfirmButton = page.locator('span.table-action').filter({ hasText: CHALLENGES_PAGE.save });
        this.editCancelButton = page.locator('span.table-action').filter({ hasText: CHALLENGES_PAGE.cancel });
        this.deleteChallengeButtons = page.locator('span.table-action').filter({hasText: CHALLENGES_PAGE.delete });
        this.popUpYes = page.locator('button.popConfirm-ok-button');
        this.popUpNo = page.locator('popConfirm-cancel-button');
        this.challengeSequenceNumberField = page.locator('input#sortNumber');
        this.challengeNameField = page.locator('input#name');
        this.challengeTitleField = page.locator('input#title');
        this.actionSuccessMessage = page.locator('div.ant-message-success');
    }

    async gotoChallengesPage() {
        await this.page.goto(CHALLENGES_ADMIN_URL);
    }

    async openAddChallengePage() {
        await this.addChallengeButton.click();
        await this.verifyUrl(ADD_CHALLENGE_ADMIN_URL);
    }

    async openTasksPage() {
        await this.openTasksButton.click();
        await this.verifyUrl(TASKS_ADMIN_URL);
    }

    // Opens the Challenge Info Page for the specified challenge sort number.
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

    /**
     * Selects a management option for the specified challenge.
     * @param {string} challengeSortNumber - The sort number of the challenge.
     * @param {Locator} option - The locator for the management option.
     */
    async selectChallengeManageOption(challengeSortNumber, option) {
        const challenge = await this.allChallengesSequenceNumbers.filter({
            has: this.page.getByText(challengeSortNumber, { exact: true }),
        });
        if (await challenge.isVisible()) {
            const row = await this.page.getByRole("row", { name: challengeSortNumber });
            await row.locator(option).click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, checks if there's a next page and recursively search
            await this.goToNextPage();
            await this.selectChallengeManageOption(challengeSortNumber, option);
        } else {
            // If the club is not found and there are no more pages, throws an error
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
        const challenge = await this.page.getByText(challengeSortNumber, { exact: true });
        await this.verifyElementVisibility(challenge, false);
    }
}

module.exports = ChallengesPage;
