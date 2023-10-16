import { expect } from "@playwright/test";
import { challengesAdminUrl, addChallengeAdminUrl, tasksAdminUrl } from "../constants/api.constants";
import BasePage from "./BasePage";

class ChallengesPage extends BasePage {
    constructor(page) {
        super(page);
        this.challengesPageTitle = page.getByRole("heading", { name: "Челенджі" });
        this.addChallengeButton = page.locator("button.add-btn");
        this.openTasksButton = page.locator("a.back-btn button");
    }

    async gotoChallengesPage() {
        await this.page.goto(challengesAdminUrl);
    }

    async openAddChallengePage() {
        await this.addChallengeButton.click();
        await expect(await this.page.url()).toBe(addChallengeAdminUrl);
    }

    async openTasksPage() {
        await this.openTasksButton.click();
        await expect(await this.page.url()).toBe(tasksAdminUrl);
    }
}

module.exports = ChallengesPage;
