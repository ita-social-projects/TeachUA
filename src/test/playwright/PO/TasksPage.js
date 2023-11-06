import { challengesAdminUrl, addTaskAdminUrl, tasksAdminUrl } from "../constants/api.constants";
import BasePage from "./BasePage";

class TasksPage extends BasePage {
    constructor(page) {
        super(page);
        this.addTaskButton = page.locator("a[href='/dev/admin/addTask']");
        this.openChallengesButton = page.locator("a.back-btn button");

        //Task information page
        this.taskInfoName = page.locator("div.task-header");
        this.taskInfoTitle = page.locator("div.task-text:nth-child(2)");
        this.taskInfoDescription = page.locator("div.task-text:nth-child(1)");
        this.allTasksNames = page.locator("tbody td:nth-child(2)");
    }

    async gotoTasksPage() {
        await this.page.goto(tasksAdminUrl);
    }

    async openAddTaskPage() {
        await this.addTaskButton.click();
        await this.verifyUrl(addTaskAdminUrl);        
    }

    async openChallengesPage() {
        await this.openChallengesButton.click();
        await this.verifyUrl(challengesAdminUrl);        
    }

}

module.exports = TasksPage;
