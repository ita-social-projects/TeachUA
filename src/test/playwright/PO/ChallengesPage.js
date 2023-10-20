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

}

module.exports = ChallengesPage;
