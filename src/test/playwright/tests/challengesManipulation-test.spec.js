import { test} from "@playwright/test";
import ApiService from "../services/ApiService";
import ChallengesPage from "../PO/ChallengesPage";
import ChallengeInfoPage from "../PO/ChallengeInfoPage";
import TasksPage from "../PO/TasksPage";
import AddChallengePage from "../PO/AddChallengePage";
import AddTaskPage from "../PO/AddTaskPage";
import {CREATE_CHALLENGE_REQUEST, CREATE_TASK_REQUEST, CREATE_TASK_REQUEST_2, CREATE_TASK_REQUEST_3, CREATE_TASK_REQUEST_4, TASKS_ADMIN_URL} from "../constants/api.constants";
import {NEW_CHALLENGE_CORRECT_DETAILS, EDITED_CHALLENGE_CORRECT_DETAILS, NEW_TASK_CORRECT_DETAILS, EDITED_TASK_CORRECT_DETAILS} from "../constants/challengeAndTaskInformation.constants";
import { USER_ROLES } from "../constants/general.constants"

let apiService, challengesPage, challengeInfoPage,tasksPage, addChallengePage, addTaskPage;

    test.beforeEach(async({page})=>{
        apiService = new ApiService(page);
        challengesPage = new ChallengesPage(page);
        await apiService.apiLoginAs(USER_ROLES.admin);
    })

    test("Verify that a new challenge can be successfully created", async ({ page }) => {
        await challengesPage.gotoChallengesPage();
        addChallengePage = new AddChallengePage(page);
        await apiService.deleteChallengeBySequenceNumber(NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
);
        await challengesPage.openAddChallengePage();
        await addChallengePage.fillInputField(addChallengePage.challengeSequenceNumberField, NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)
        await addChallengePage.fillInputField(addChallengePage.challengeNameField, NEW_CHALLENGE_CORRECT_DETAILS.name)
        await addChallengePage.fillInputField(addChallengePage.challengeTitleField, NEW_CHALLENGE_CORRECT_DETAILS.title)
        await addChallengePage.fillInputField(addChallengePage.challengeDescriptionField, NEW_CHALLENGE_CORRECT_DETAILS.description)
        await addChallengePage.uploadChallengePhoto();
        await addChallengePage.confirmChallengeCreation();

        await addChallengePage.verifyElementVisibility(addChallengePage.challengeAddedSuccessMessage);
        await addChallengePage.openChallengesPage();
        await addChallengePage.verifyElementExistance(challengesPage.allChallengesSequenceNumbers, NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)

    });


    test("Verify that a new task can be successfully added to an existing challenge", async ({ page }) => {
        tasksPage = new TasksPage(page);
        addTaskPage = new AddTaskPage(page);
        challengeInfoPage = new ChallengeInfoPage(page);
        await apiService.deleteTaskByName(NEW_TASK_CORRECT_DETAILS.name);

        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await tasksPage.gotoTasksPage();
        await tasksPage.openAddTaskPage();

        await addTaskPage.selectDate(NEW_TASK_CORRECT_DETAILS.startDate);
        await addTaskPage.uploadTaskPhoto();
        await addTaskPage.fillInputField(addTaskPage.taskNameField, NEW_TASK_CORRECT_DETAILS.name);
        await addTaskPage.fillInputField(addTaskPage.taskTitleField, NEW_TASK_CORRECT_DETAILS.title);
        await addTaskPage.fillInputField(addTaskPage.taskDescriptionField, NEW_TASK_CORRECT_DETAILS.description);
        await addTaskPage.selectChallenge(CREATE_CHALLENGE_REQUEST.body.name);

        await addTaskPage.confirmTaskCreation();

        await addTaskPage.verifyElementVisibility(addTaskPage.taskAddedSuccessMessage);

        await tasksPage.gotoTasksPage();
        await tasksPage.verifyElementExistance(tasksPage.allTasksNames, NEW_TASK_CORRECT_DETAILS.name);

        await tasksPage.openChallengesPage();
        await challengesPage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengeInfoPage.verifyElementExistance(
            challengeInfoPage.tasksTableCells,
            NEW_TASK_CORRECT_DETAILS.name
        );
    });


    test("Verify that a challenge can be succesfully deleted", async ({ page }) => {
        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await challengesPage.gotoChallengesPage();
        await challengesPage.deleteChallenge(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengesPage.verifyElementVisibility(challengesPage.actionSuccessMessage);
        await challengesPage.verifyElementExistance(
            challengesPage.allChallengesSequenceNumbers,
            CREATE_CHALLENGE_REQUEST.body.sortNumber,
            false
        );
    });

    test("Verify that an existing challenge can be edited on the challenges list page", async ({ page }) => {
        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiService.deleteChallengeBySequenceNumber(EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber);
        await challengesPage.gotoChallengesPage();
        await challengesPage.turnOnEditChallenge(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengesPage.fillInputField(
            challengesPage.challengeSequenceNumberField,
            EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
        );
        await challengesPage.fillInputField(challengesPage.challengeNameField, EDITED_CHALLENGE_CORRECT_DETAILS.name);
        await challengesPage.fillInputField(challengesPage.challengeTitleField, EDITED_CHALLENGE_CORRECT_DETAILS.title);
        await challengesPage.confirmEditChallenge();
        await challengesPage.verifyElementExistance(
            challengesPage.allChallengesSequenceNumbers,
            EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber,
            true
        );
    });

    test("Verify that an existing challenge can be edited", async ({ page }) => {
        addChallengePage = new AddChallengePage(page);
        await apiService.deleteChallengeBySequenceNumber(EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
);
        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await challengesPage.gotoChallengesPage();
        await challengesPage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await addChallengePage.fillInputField(addChallengePage.challengeSequenceNumberField, EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)
        await addChallengePage.fillInputField(addChallengePage.challengeNameField, EDITED_CHALLENGE_CORRECT_DETAILS.name)
        await addChallengePage.fillInputField(addChallengePage.challengeTitleField, EDITED_CHALLENGE_CORRECT_DETAILS.title)
        await addChallengePage.fillInputField(addChallengePage.challengeDescriptionField, EDITED_CHALLENGE_CORRECT_DETAILS.description)
        await addChallengePage.confirmChallengeCreation();
        await addChallengePage.verifyElementVisibility(addChallengePage.challengeAddedSuccessMessage);
        await challengesPage.gotoChallengesPage();
        await challengesPage.verifyElementExistance(challengesPage.allChallengesSequenceNumbers, EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
, true);
    });
    
    test("Verify that a task can be succesfully deleted", async({page})=>{
        tasksPage = new TasksPage(page);
        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiService.createNewTask(CREATE_TASK_REQUEST);
        await tasksPage.gotoTasksPage();
        await tasksPage.deleteTask(CREATE_TASK_REQUEST.body.name);
        await tasksPage.verifyElementVisibility(tasksPage.actionSuccessMessage);
        
        // Verification below won't work if there a couple of tasks with the same name
        await tasksPage.verifyElementExistance(
            tasksPage.allTasksNames,
            CREATE_TASK_REQUEST.body.name, false
        );
    })

    test("Verify that an existing task can be edited on the tasks list page", async({page})=>{
        tasksPage = new TasksPage(page);
        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiService.deleteTaskByName(EDITED_TASK_CORRECT_DETAILS.name)
        await apiService.createNewTask(CREATE_TASK_REQUEST);
        await tasksPage.gotoTasksPage();
        await tasksPage.turnOnEditTask(CREATE_TASK_REQUEST.body.name);
        await tasksPage.fillInputField(tasksPage.taskNameField, EDITED_TASK_CORRECT_DETAILS.name)
        await tasksPage.confirmEditTask();
        await tasksPage.verifyElementExistance(
            challengesPage.allChallengesSequenceNumbers,
            EDITED_TASK_CORRECT_DETAILS.name,
            true
        );
     })


    test("Verify that an existing task can be edited", async ({ page }) => {
        tasksPage = new TasksPage(page);
        addTaskPage = new AddTaskPage(page);
        await apiService.deleteTaskByName(EDITED_TASK_CORRECT_DETAILS.name);
        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiService.createNewTask(CREATE_TASK_REQUEST);
        await tasksPage.gotoTasksPage();
        await tasksPage.openTaskInfoPage(CREATE_TASK_REQUEST.body.name);
        await addTaskPage.fillInputField(addTaskPage.taskNameField, EDITED_TASK_CORRECT_DETAILS.name);
        await addTaskPage.fillInputField(addTaskPage.taskTitleField, EDITED_TASK_CORRECT_DETAILS.title);
        await addTaskPage.fillInputField(addTaskPage.taskDescriptionField, EDITED_TASK_CORRECT_DETAILS.description);
        await addTaskPage.confirmTaskCreation();
        await addTaskPage.verifyElementVisibility(addTaskPage.taskAddedSuccessMessage);
        await tasksPage.gotoTasksPage();
        await tasksPage.verifyElementExistance(tasksPage.allTasksNames, EDITED_TASK_CORRECT_DETAILS.name, true);
    });


    test("Verify that the user can navigate through challenge's existing tasks", async({page})=>{
        tasksPage = new TasksPage(page);
        challengeInfoPage = new ChallengeInfoPage(page);
        await apiService.deleteTaskByName(CREATE_TASK_REQUEST.body.name);
        await apiService.deleteTaskByName(CREATE_TASK_REQUEST_2.body.name);
        await apiService.deleteTaskByName(CREATE_TASK_REQUEST_3.body.name);
        await apiService.deleteTaskByName(CREATE_TASK_REQUEST_4.body.name);

        await apiService.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiService.createNewTask(CREATE_TASK_REQUEST);
        await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST.body.name);
        await apiService.createNewTask(CREATE_TASK_REQUEST_2);
        await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST_2.body.name);
        await apiService.createNewTask(CREATE_TASK_REQUEST_3);
        await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST_3.body.name);
        await apiService.createNewTask(CREATE_TASK_REQUEST_4);
        await apiService.changeTaskDateToToday(CREATE_TASK_REQUEST_4.body.name);
        await challengesPage.gotoChallengesPage();
        await challengesPage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengeInfoPage.openViewChallenge();
        await challengeInfoPage.verifyTaskOnChallengeView(CREATE_TASK_REQUEST_4.body.name);
    })


    test.afterEach(async({page})=>{
        await page.close();
    })
