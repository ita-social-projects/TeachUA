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

        this.firstTask = page.locator("tr:first-child a.table-name");
        this.editTaskButtons = page.locator("span.table-action:nth-child(1)");
        this.editConfirmButton = page.locator("span.table-action").filter({ hasText: "Зберегти" });
        this.editCancelButton = page.locator("span.table-action").filter({ hasText: "Відмінити" });

        this.deleteTaskButtons = page.locator("span.table-action:nth-child(2)");
        //insecure locator below (each time 'delete' button was clicked, it created a new instance). No unique locator available
        this.popUpYes = page.locator("button.popConfirm-ok-button");
        this.popUpNo = page.locator("popConfirm-cancel-button");

        this.taskNameField = page.locator("input#name");
        this.taskChallengeField = page.locator("input#challengeId");
        this.taskIsActiveCheckbox = page.locator("input#isActive");

        this.actionSuccessMessage = page.locator("div.ant-message-success");
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

    async openTaskInfoPage(taskName) {
        await this.verifyElementVisibility(this.firstTask);

        const task = await this.allTasksNames.filter({
            has: this.page.getByText(taskName, { exact: true }),
        });
        if (await task.first().isVisible()) {
            await task.locator('a.table-name').first().click();
            return;
        } else if (await this.isNextPageAvailable()) {
            await this.goToNextPage();
            await this.openTaskInfoPage(taskName);
        } else {
            throw new Error("No such task exist");
        }
    }

    async selectTaskManageOption(taskName, option) {
        const task = await this.allTasksNames.filter({
            has: this.page.getByText(taskName, { exact: true }),
        });
        if (await task.first().isVisible()) {
            const row = await this.page.getByRole("row", { name: taskName }).first();
            await row.locator(option).click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, check if there's a next page and recursively search
            await this.goToNextPage();
            await this.selectTaskManageOption(challengeSortNumber, option);
        } else {
            // If the club is not found and there are no more pages, throw an error
            throw new Error("No such task exists");
        }
    }

    async turnOnEditTask(taskName) {
        await this.selectTaskManageOption(taskName + "", this.editTaskButtons);
        await this.verifyElementVisibility(this.taskNameField);
    }

    async confirmEditTask() {
        await this.editConfirmButton.click();
        await this.popUpYes.click();
        await this.verifyElementVisibility(this.editConfirmButton, false);
        await this.verifyElementVisibility(this.editCancelButton, false);
    }

    async deleteTask(taskName) {
        await this.selectTaskManageOption(taskName + "", this.deleteTaskButtons);
        await this.popUpYes.click();
        const task = await this.page.getByText(taskName, { exact: true })
        await this.verifyElementVisibility(task, false);
    }

}

module.exports = TasksPage;
