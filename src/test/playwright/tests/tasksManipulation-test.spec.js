import { test, chromium } from "@playwright/test";
import ApiService from "../services/ApiService";
import ChallengeInfoPage from "../PO/ChallengeInfoPage";
import TasksPage from "../PO/TasksPage";
import AddTaskPage from "../PO/AddTaskPage";
import ChallengesPage from "../PO/ChallengesPage";
import {
    CREATE_CHALLENGE_REQUEST_2,
    CREATE_TASK_REQUEST,
    CREATE_TASK_REQUEST_2,
    CREATE_TASK_REQUEST_3,
    CREATE_TASK_REQUEST_4,
} from "../constants/api.constants";
import {
    NEW_TASK_CORRECT_DETAILS,
    EDITED_TASK_CORRECT_DETAILS,
} from "../constants/challengeAndTaskInformation.constants";
import { USER_ROLES } from "../constants/general.constants";

let page, apiService, challengesPage, challengeInfoPage, tasksPage, addTaskPage;

test.beforeAll(async () => {
    const browser = await chromium.launch();
    const context = await browser.newContext();
    page = await context.newPage();
    apiService = new ApiService(page);
    tasksPage = new TasksPage(page);
    addTaskPage = new AddTaskPage(page);
    challengeInfoPage = new ChallengeInfoPage(page);
    challengesPage = new ChallengesPage(page);

    await apiService.apiLoginAs(USER_ROLES.admin);
    await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST_2);
});

test.beforeEach(async () => {
    await apiService.createNewTask(CREATE_TASK_REQUEST);
    await tasksPage.gotoTasksPage();
});

test("Verify that a new task can be successfully added to an existing challenge", async () => {
    await apiService.deleteTaskByName(NEW_TASK_CORRECT_DETAILS.name);
    await tasksPage.openAddTaskPage();
    await addTaskPage.selectDate(NEW_TASK_CORRECT_DETAILS.startDate);
    await addTaskPage.uploadTaskPhoto();
    await addTaskPage.fillInputField(addTaskPage.taskNameField, NEW_TASK_CORRECT_DETAILS.name);
    await addTaskPage.fillInputField(addTaskPage.taskTitleField, NEW_TASK_CORRECT_DETAILS.title);
    await addTaskPage.fillInputField(addTaskPage.taskDescriptionField, NEW_TASK_CORRECT_DETAILS.description);
    await addTaskPage.selectChallenge(CREATE_CHALLENGE_REQUEST_2.body.name);
    await addTaskPage.confirmTaskCreation();

    await addTaskPage.verifyElementVisibility(addTaskPage.taskAddedSuccessMessage);

    await tasksPage.gotoTasksPage();
    await tasksPage.verifyElementExistance(tasksPage.allTasksNames, NEW_TASK_CORRECT_DETAILS.name);

    await tasksPage.openChallengesPage();
    await challengesPage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST_2.body.sortNumber);
    await challengeInfoPage.verifyElementExistance(challengeInfoPage.tasksTableCells, NEW_TASK_CORRECT_DETAILS.name);
});

test("Verify that a task can be succesfully deleted", async () => {
    await tasksPage.deleteTask(CREATE_TASK_REQUEST.body.name);
    await tasksPage.verifyElementVisibility(tasksPage.actionSuccessMessage);

    // Verification below won't work if there a couple of tasks with the same name
    await tasksPage.verifyElementExistance(tasksPage.allTasksNames, CREATE_TASK_REQUEST.body.name, false);
});

test("Verify that an existing task can be edited on the tasks list page", async () => {
    await apiService.deleteTaskByName(EDITED_TASK_CORRECT_DETAILS.name);
    await tasksPage.turnOnEditTask(CREATE_TASK_REQUEST.body.name);
    await tasksPage.fillInputField(tasksPage.taskNameField, EDITED_TASK_CORRECT_DETAILS.name);
    await tasksPage.confirmEditTask();
    await tasksPage.verifyElementExistance(
        challengesPage.allChallengesSequenceNumbers,
        EDITED_TASK_CORRECT_DETAILS.name,
        true
    );
});

test("Verify that an existing task can be edited", async () => {
    await apiService.deleteTaskByName(EDITED_TASK_CORRECT_DETAILS.name);
    await tasksPage.openTaskInfoPage(CREATE_TASK_REQUEST.body.name);
    await addTaskPage.fillInputField(addTaskPage.taskNameField, EDITED_TASK_CORRECT_DETAILS.name);
    await addTaskPage.fillInputField(addTaskPage.taskTitleField, EDITED_TASK_CORRECT_DETAILS.title);
    await addTaskPage.fillInputField(addTaskPage.taskDescriptionField, EDITED_TASK_CORRECT_DETAILS.description);
    await addTaskPage.confirmTaskCreation();
    await addTaskPage.verifyElementVisibility(addTaskPage.taskAddedSuccessMessage);
    await tasksPage.gotoTasksPage();
    await tasksPage.verifyElementExistance(tasksPage.allTasksNames, EDITED_TASK_CORRECT_DETAILS.name, true);
});

test("Verify that the user can navigate through challenge's existing tasks", async () => {
    await apiService.deleteTaskByName(CREATE_TASK_REQUEST_2.body.name);
    await apiService.deleteTaskByName(CREATE_TASK_REQUEST_3.body.name);
    await apiService.deleteTaskByName(CREATE_TASK_REQUEST_4.body.name);

    await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST.body.name);
    await apiService.createNewTask(CREATE_TASK_REQUEST_2);
    await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST_2.body.name);
    await apiService.createNewTask(CREATE_TASK_REQUEST_3);
    await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST_3.body.name);
    await apiService.createNewTask(CREATE_TASK_REQUEST_4);
    await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST_4.body.name);
    await challengesPage.gotoChallengesPage();
    await challengesPage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST_2.body.sortNumber);
    await challengeInfoPage.openViewChallenge();
    await challengeInfoPage.verifyTaskOnChallengeView(CREATE_TASK_REQUEST_4.body.name);
});

test.afterAll(async () => {
    await page.close();
});