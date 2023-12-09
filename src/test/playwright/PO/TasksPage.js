import { CHALLENGES_ADMIN_URL, ADD_TASK_ADMIN_URL, TASKS_ADMIN_URL } from "../constants/api.constants";
import {TASKS_PAGE} from "../constants/locatorsText.constants";
import BasePage from "./BasePage";

class TasksPage extends BasePage {
    constructor(page) {
        super(page);
        this.addTaskButton = page.locator('a[href="/dev/admin/addTask"]');
        this.openChallengesButton = page.locator('a.back-btn button');
        this.taskInfoName = page.locator('div.task-header');
        this.allTasksNames = page.locator('td.ant-table-cell > a.table-name[href*="task"]');
        this.firstTask = page.locator('tr:first-child a.table-name');
        this.editTaskButtons = page.locator('span.table-action').filter({hasText: TASKS_PAGE.edit});
        this.editConfirmButton = page.locator('span.table-action').filter({ hasText: TASKS_PAGE.confirmEdit});
        this.editCancelButton = page.locator('span.table-action').filter({ hasText: TASKS_PAGE.cancel});
        this.deleteTaskButtons = page.locator('span.table-action').filter({hasText: TASKS_PAGE.delete});
        this.popUpYes = page.locator('button.popConfirm-ok-button');
        this.popUpNo = page.locator('popConfirm-cancel-button');
        this.taskNameField = page.locator('input#name');
        this.taskChallengeField = page.locator('input#challengeId');
        this.taskIsActiveCheckbox = page.locator('input#isActive');
        this.actionSuccessMessage = page.locator('div.ant-message-success');
    }

    async gotoTasksPage() {
        await this.page.goto(TASKS_ADMIN_URL);
    }

    async openAddTaskPage() {
        await this.addTaskButton.click();
        await this.verifyUrl(ADD_TASK_ADMIN_URL);
    }

    async openChallengesPage() {
        await this.openChallengesButton.click();
        await this.verifyUrl(CHALLENGES_ADMIN_URL);
    }

    // Opens the page with details of a specific task by clicking on its name.
    async openTaskInfoPage(taskName) {
        // Check if there is at least one task present before proceeding
        await this.verifyElementVisibility(this.firstTask);

        // Searches for the task by name and clicks on it
        const task = await this.allTasksNames.filter({
            has: this.page.getByText(taskName, { exact: true }),
        });
        if (await task.first().isVisible()) {
            await task.click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the task is not found on this page, checks if there's a next page and recursively searches
            await this.goToNextPage();
            await this.openTaskInfoPage(taskName);
        } else {
            // If the task is not found and there are no more pages, throws an error
            throw new Error("No such task exist");
        }
    }

    /**
     * Selects a management option for a task, such as edit or delete
     * @param {string} taskName - The name of the task for which the option is selected.
     * @param {Locator} option - The Playwright Locator representing the option to be selected.
     */
    async selectTaskManageOption(taskName, option) {
        // Searches for the task by name and selects the specified option
        const task = await this.allTasksNames.filter({
            has: this.page.getByText(taskName, { exact: true }),
        });

        // In case there are multiple tasks with the same name, first() selects the first one
        if (await task.first().isVisible()) {
            const row = await this.page.getByRole("row", { name: taskName }).first();
            await row.locator(option).click();
            return;
        } else if (await this.isNextPageAvailable()) {
            // If the club is not found on this page, checks if there's a next page and recursively search
            await this.goToNextPage();
            await this.selectTaskManageOption(taskName, option);
        } else {
            // If the club is not found and there are no more pages, throws an error
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
        const task = await this.page.getByText(taskName, { exact: true });
        await this.verifyElementVisibility(task, false);
    }
}

module.exports = TasksPage;
