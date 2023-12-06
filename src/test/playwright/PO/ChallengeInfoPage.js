
import BasePage from "./BasePage";
import { expect } from "@playwright/test";
import { CHALLENGE_INFO_PAGE } from "../constants/locatorsText.constants";

class ChallengeInfoPage extends BasePage {
    constructor(page) {
        super(page);
        this.viewChallengeButton = page.getByRole('button', { name: CHALLENGE_INFO_PAGE.viewChallenge });
        this.viewChallengeTitle = page.locator('span.title');
        this.slickCard = page.locator('div.slick-slide');
        this.slickCardsNames = page.locator('div.slick-slide div.name');
        this.slickDots = page.locator('ul.slick-dots li');
        this.slickRightArrow = page.locator('span.arrows-next svg');
        this.challengePageTitle = page.locator('h1.ant-typography');
        this.challengeSortNumber = page.locator('input#sortNumber');
        this.challengeStatus = page.locator('button#isActive');
        this.challengeName = page.locator('input#name');
        this.challengeTitle = page.locator('input#title');
        this.challengeDescription = page.locator('div.ql-editor');
        this.saveButton = page.locator('button.flooded-button[type="submit"]');
        this.tasksTableCells = page.locator('td[class="ant-table-cell"]');
    }

    async openViewChallenge() {
        await this.viewChallengeButton.click();
        await this.verifyElementVisibility(this.viewChallengeTitle);
    }

    async verifyTaskOnChallengeView(taskName) {
        // Step 1: Verify the existence of the task in the slick carousel
        await this.verifyElementExistance(this.slickCardsNames, taskName);

        // Step 2: Find the target card based on the provided taskName
        const targetCard = await this.slickCard.filter({ hasText: taskName });

        // Step 3: Loop through challenge tasks until the target card is active
        await this.navigateToTargetTask(targetCard);
    }

    async navigateToTargetTask(targetCard) {
        const dotsCount = await this.slickDots.count();

        let i = 0;
        while (i < dotsCount && !(await this.isCardActive(targetCard))) {
            await this.slickRightArrow.click();
            i++;
        }
        expect(await this.isCardActive(targetCard)).toBeTruthy();
    }

    async isCardActive(card) {
        const slickClass = await card.getAttribute("class");
        return slickClass.includes("slick-active");
    }
}

module.exports = ChallengeInfoPage;