import { expect } from "@playwright/test";
import { challengesAdminUrl, addChallengeAdminUrl, tasksAdminUrl } from "../constants/api.constants";
import BasePage from "./BasePage";

class ChallengesPage extends BasePage {
    constructor(page) {
        super(page);
        this.challengesPageTitle = page.getByRole("heading", { name: "Челенджі" });
        this.addChallengeButton = page.locator("button.add-btn");
        this.openTasksButton = page.locator("a.back-btn button");
        this.allChallengesSequenceNumbers = page.locator("tbody td:nth-child(2)");
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

    async isClubPresent(clubTitle) {
        try {
            await (await this.firstClub).waitFor({ state: "visible", timeout: 5000 });
        } catch (e) {
            console.log("Current user doesn't own any clubs");
            return false;
        }

        const clubNames = await this.clubsNames.allTextContents();
        if (await clubNames.includes(clubTitle)) {
            return true;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            return await this.isClubPresent(clubTitle);
        } else {
            // If the club is not found and there are no more pages, return false
            return false;
        }
    }
}

module.exports = ChallengesPage;
