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

let apiservice, challengespage, challengeinfopage,taskspage, addchallengepage, addtaskpage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        challengespage = new ChallengesPage(page);
        await apiservice.apiLoginAs(USER_ROLES.admin);
    })

    test("Verify that a new challenge can be successfully created", async ({ page }) => {
        await challengespage.gotoChallengesPage();
        addchallengepage = new AddChallengePage(page);
        await apiservice.deleteChallengeBySequenceNumber(NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
);
        await challengespage.openAddChallengePage();
        await addchallengepage.fillInputField(addchallengepage.challengeSequenceNumberField, NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)
        await addchallengepage.fillInputField(addchallengepage.challengeNameField, NEW_CHALLENGE_CORRECT_DETAILS.name)
        await addchallengepage.fillInputField(addchallengepage.challengeTitleField, NEW_CHALLENGE_CORRECT_DETAILS.title)
        await addchallengepage.fillInputField(addchallengepage.challengeDescriptionField, NEW_CHALLENGE_CORRECT_DETAILS.description)
        await addchallengepage.uploadChallengePhoto();
        await addchallengepage.confirmChallengeCreation();
       // await addchallengepage.confirmChallengeCreation();

        await addchallengepage.verifyElementVisibility(addchallengepage.challengeAddedSuccessMessage);
        await addchallengepage.openChallengesPage();
        await addchallengepage.verifyElementExistance(challengespage.allChallengesSequenceNumbers, NEW_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)

    });


    test("Verify that a new task can be successfully added to an existing challenge", async ({ page }) => {
        taskspage = new TasksPage(page);
        addtaskpage = new AddTaskPage(page);
        challengeinfopage = new ChallengeInfoPage(page);
        await apiservice.deleteTaskByName(NEW_TASK_CORRECT_DETAILS.name);

        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await taskspage.gotoTasksPage();
        await taskspage.openAddTaskPage();
        
        await addtaskpage.selectDate(NEW_TASK_CORRECT_DETAILS.startDate);
        await addtaskpage.uploadTaskPhoto();
        await addtaskpage.fillInputField(addtaskpage.taskNameField, NEW_TASK_CORRECT_DETAILS.name)
        await addtaskpage.fillInputField(addtaskpage.taskTitleField, NEW_TASK_CORRECT_DETAILS.title)
        await addtaskpage.fillInputField(addtaskpage.taskDescriptionField, NEW_TASK_CORRECT_DETAILS.description)
        await addtaskpage.selectChallenge(CREATE_CHALLENGE_REQUEST.body.name)

        await addtaskpage.confirmTaskCreation();

        await addtaskpage.verifyElementVisibility(addtaskpage.taskAddedSuccessMessage);
        
        /*
        *identical tasks can be created, so there is little sense in task creation verification yet
        *same situation with task deletion using API, can't be implemented as multiple tasks
        *can exist with the same name
        */
       
        await taskspage.gotoTasksPage();
        await taskspage.verifyElementExistance(taskspage.allTasksNames, NEW_TASK_CORRECT_DETAILS.name)
        
        await taskspage.openChallengesPage();
        await challengespage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengeinfopage.verifyElementExistance(challengeinfopage.tasksTableCells, NEW_TASK_CORRECT_DETAILS.name);
    });


    test("Verify that a challenge can be succesfully deleted", async ({ page }) => {
        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await challengespage.gotoChallengesPage();
        await challengespage.deleteChallenge(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengespage.verifyElementVisibility(challengespage.actionSuccessMessage);
        await challengespage.verifyElementExistance(
            challengespage.allChallengesSequenceNumbers,
            CREATE_CHALLENGE_REQUEST.body.sortNumber,
            false
        );
    });

    test("Verify that an existing challenge can be edited on the challenges list page", async({page})=>{
        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiservice.deleteChallengeBySequenceNumber(EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)
        await challengespage.gotoChallengesPage();
        await challengespage.turnOnEditChallenge(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengespage.fillInputField(challengespage.challengeSequenceNumberField, EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)
        await challengespage.fillInputField(challengespage.challengeNameField, EDITED_CHALLENGE_CORRECT_DETAILS.name)
        await challengespage.fillInputField(challengespage.challengeTitleField, EDITED_CHALLENGE_CORRECT_DETAILS.title)
        await challengespage.confirmEditChallenge();
        await challengespage.verifyElementExistance(
            challengespage.allChallengesSequenceNumbers,
            EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
,
            true
        );
    })

    test("Verify that an existing challenge can be edited", async ({ page }) => {
        addchallengepage = new AddChallengePage(page);
        await apiservice.deleteChallengeBySequenceNumber(EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
);
        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await challengespage.gotoChallengesPage();
        await challengespage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await addchallengepage.fillInputField(addchallengepage.challengeSequenceNumberField, EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
)
        await addchallengepage.fillInputField(addchallengepage.challengeNameField, EDITED_CHALLENGE_CORRECT_DETAILS.name)
        await addchallengepage.fillInputField(addchallengepage.challengeTitleField, EDITED_CHALLENGE_CORRECT_DETAILS.title)
        await addchallengepage.fillInputField(addchallengepage.challengeDescriptionField, EDITED_CHALLENGE_CORRECT_DETAILS.description)
        await addchallengepage.confirmChallengeCreation();
        await addchallengepage.verifyElementVisibility(addchallengepage.challengeAddedSuccessMessage);
        await challengespage.gotoChallengesPage();
        await challengespage.verifyElementExistance(challengespage.allChallengesSequenceNumbers, EDITED_CHALLENGE_CORRECT_DETAILS.sequenceNumber
, true);
    });
    
    test("Verify that a task can be succesfully deleted", async({page})=>{
        taskspage = new TasksPage(page);
        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiservice.createNewTask(CREATE_TASK_REQUEST);
        await taskspage.gotoTasksPage();
        await taskspage.deleteTask(CREATE_TASK_REQUEST.body.name);
        await taskspage.verifyElementVisibility(taskspage.actionSuccessMessage);
        
        /*Verification below won't work if there a couple of tasks with the same name*/
        await taskspage.verifyElementExistance(
            taskspage.allTasksNames,
            CREATE_TASK_REQUEST.body.name, false
        );
    })

    test("Verify that an existing task can be edited on the tasks list page", async({page})=>{
        taskspage = new TasksPage(page);
        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiservice.deleteTaskByName(EDITED_TASK_CORRECT_DETAILS.name)
        await apiservice.createNewTask(CREATE_TASK_REQUEST);
        await taskspage.gotoTasksPage();
        await taskspage.turnOnEditTask(CREATE_TASK_REQUEST.body.name);
        await taskspage.fillInputField(taskspage.taskNameField, EDITED_TASK_CORRECT_DETAILS.name)
        await taskspage.confirmEditTask();
        await taskspage.verifyElementExistance(
            challengespage.allChallengesSequenceNumbers,
            EDITED_TASK_CORRECT_DETAILS.name,
            true
        );
     })


    test("Verify that an existing task can be edited", async ({ page }) => {
        taskspage = new TasksPage(page);
        addtaskpage = new AddTaskPage(page);
        await apiservice.deleteTaskByName(EDITED_TASK_CORRECT_DETAILS.name);
        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiservice.createNewTask(CREATE_TASK_REQUEST);
        await taskspage.gotoTasksPage();
        await taskspage.openTaskInfoPage(CREATE_TASK_REQUEST.body.name);
        await addtaskpage.fillInputField(addtaskpage.taskNameField, EDITED_TASK_CORRECT_DETAILS.name);
        await addtaskpage.fillInputField(addtaskpage.taskTitleField, EDITED_TASK_CORRECT_DETAILS.title);
        await addtaskpage.fillInputField(addtaskpage.taskDescriptionField, EDITED_TASK_CORRECT_DETAILS.description);
        await addtaskpage.confirmTaskCreation();
        await addtaskpage.verifyElementVisibility(addtaskpage.taskAddedSuccessMessage);
        await taskspage.gotoTasksPage();
        await taskspage.verifyElementExistance(taskspage.allTasksNames, EDITED_TASK_CORRECT_DETAILS.name, true);
    });


    test("Verify that the user can navigate through challenge's existing tasks", async({page})=>{
        taskspage = new TasksPage(page);
        challengeinfopage = new ChallengeInfoPage(page);
        await apiservice.deleteTaskByName(CREATE_TASK_REQUEST.body.name);
        await apiservice.deleteTaskByName(CREATE_TASK_REQUEST_2.body.name);
        await apiservice.deleteTaskByName(CREATE_TASK_REQUEST_3.body.name);
        await apiservice.deleteTaskByName(CREATE_TASK_REQUEST_4.body.name);

        await apiservice.createNewChallenge(CREATE_CHALLENGE_REQUEST);
        await apiservice.createNewTask(CREATE_TASK_REQUEST);
        await apiservice.changeTaskDateToToday(CREATE_TASK_REQUEST.body.name);
        await apiservice.createNewTask(CREATE_TASK_REQUEST_2);
        await apiservice.changeTaskDateToToday(CREATE_TASK_REQUEST_2.body.name);
        await apiservice.createNewTask(CREATE_TASK_REQUEST_3);
        await apiservice.changeTaskDateToToday(CREATE_TASK_REQUEST_3.body.name);
        await apiservice.createNewTask(CREATE_TASK_REQUEST_4);
        await apiservice.changeTaskDateToToday(CREATE_TASK_REQUEST_4.body.name);
        await challengespage.gotoChallengesPage();
        await challengespage.openChallengeInfoPage(CREATE_CHALLENGE_REQUEST.body.sortNumber);
        await challengeinfopage.openViewChallenge();
        await challengeinfopage.verifyTaskOnChallengeView(CREATE_TASK_REQUEST_4.body.name);
    })


    test.afterEach(async({page})=>{
        await page.close();
    })
