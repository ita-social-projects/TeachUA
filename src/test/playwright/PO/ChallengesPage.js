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
        this.firstChallenge = page.locator("tbody tr:first-child");
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



    async checkIfChallengesExist() {
        try {
            await (await this.firstChallenge).waitFor({ state: "visible", timeout: 5000 });
        } catch (e) {
            throw new Error("There are no challenges existing");
        }
    }

    async openChallengeInfoPage(challengeSortNumber) {
        await this.checkIfChallengesExist();

        const challenge = await this.allChallengesSequenceNumbers.filter({ has: this.page.getByText(challengeSortNumber, { exact: true }) });
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

}

module.exports = ChallengesPage;
