import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import ChallengesPage from "../PO/ChallengesPage";
import ChallengeInfoPage from "../PO/ChallengeInfoPage";
import TasksPage from "../PO/TasksPage";
import AddChallengePage from "../PO/AddChallengePage";
import AddTaskPage from "../PO/AddTaskPage";
import {createChallengeRequest, createTaskRequest, createTaskRequest2, createTaskRequest3, createTaskRequest4, tasksAdminUrl} from "../constants/api.constants";
import {newChallengeCorrectDetails, editedChallengeCorrectDetails, newTaskCorrectDetails, editedTaskCorrectDetails} from "../constants/challengeAndTaskInformation.constants";


let apiservice, challengespage, challengeinfopage,taskspage, addchallengepage, addtaskpage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        challengespage = new ChallengesPage(page);
        await apiservice.apiLoginAs('admin');
        // await apiservice.deleteChallengeBySequenceNumber(newChallengeCorrectDetails.SEQUENCE_NUMBER)
        // await apiservice.deleteChallengeBySequenceNumber(editedChallengeCorrectDetails.SEQUENCE_NUMBER)
        // await apiservice.deleteTaskByName(editedTaskCorrectDetails.NAME);
        // await apiservice.deleteTaskByName(newTaskCorrectDetails.NAME);
    })

    test("Verify that a new challenge can be successfully created", async ({ page }) => {
        await challengespage.gotoChallengesPage();
        addchallengepage = new AddChallengePage(page);
        await apiservice.deleteChallengeBySequenceNumber(newChallengeCorrectDetails.SEQUENCE_NUMBER);
        await challengespage.openAddChallengePage();
        await addchallengepage.fillInputField(addchallengepage.challengeSequenceNumberField, newChallengeCorrectDetails.SEQUENCE_NUMBER)
        await addchallengepage.fillInputField(addchallengepage.challengeNameField, newChallengeCorrectDetails.NAME)
        await addchallengepage.fillInputField(addchallengepage.challengeTitleField, newChallengeCorrectDetails.TITLE)
        await addchallengepage.fillInputField(addchallengepage.challengeDescriptionField, newChallengeCorrectDetails.DESCRIPTION)
        await addchallengepage.uploadChallengePhoto();
        await addchallengepage.confirmChallengeCreation();
       // await addchallengepage.confirmChallengeCreation();

        await addchallengepage.verifyElementVisibility(addchallengepage.challengeAddedSuccessMessage);
        await addchallengepage.openChallengesPage();
        await addchallengepage.verifyElementExistance(challengespage.allChallengesSequenceNumbers, newChallengeCorrectDetails.SEQUENCE_NUMBER)

    });


    test("Verify that a new task can be successfully added to an existing challenge", async ({ page }) => {
        taskspage = new TasksPage(page);
        addtaskpage = new AddTaskPage(page);
        challengeinfopage = new ChallengeInfoPage(page);
        await apiservice.deleteTaskByName(newTaskCorrectDetails.NAME);

        await apiservice.createNewChallenge(createChallengeRequest);
        await taskspage.gotoTasksPage();
        await taskspage.openAddTaskPage();
        
        await addtaskpage.selectDate(newTaskCorrectDetails.START_DATE);
        await addtaskpage.uploadTaskPhoto();
        await addtaskpage.fillInputField(addtaskpage.taskNameField, newTaskCorrectDetails.NAME)
        await addtaskpage.fillInputField(addtaskpage.taskTitleField, newTaskCorrectDetails.TITLE)
        await addtaskpage.fillInputField(addtaskpage.taskDescriptionField, newTaskCorrectDetails.DESCRIPTION)
        await addtaskpage.selectChallenge(createChallengeRequest.body.name)

        await addtaskpage.confirmTaskCreation();

        await addtaskpage.verifyElementVisibility(addtaskpage.taskAddedSuccessMessage);
        
        /*
        *identical tasks can be created, so there is little sense in task creation verification yet
        *same situation with task deletion using API, can't be implemented as multiple tasks
        *can exist with the same name
        */
       
        await taskspage.gotoTasksPage();
        await taskspage.verifyElementExistance(taskspage.allTasksNames, newTaskCorrectDetails.NAME)
        
        await taskspage.openChallengesPage();
        await challengespage.openChallengeInfoPage(createChallengeRequest.body.sortNumber);
        await challengeinfopage.verifyElementExistance(challengeinfopage.challengeTasksNames, newTaskCorrectDetails.NAME);
    });


    test("Verify that a challenge can be succesfully deleted", async ({ page }) => {
        await apiservice.createNewChallenge(createChallengeRequest);
        await challengespage.gotoChallengesPage();
        await challengespage.deleteChallenge(createChallengeRequest.body.sortNumber);
        await challengespage.verifyElementVisibility(challengespage.actionSuccessMessage);
        await challengespage.verifyElementExistance(
            challengespage.allChallengesSequenceNumbers,
            createChallengeRequest.body.sortNumber,
            false
        );
    });

    test("Verify that an existing challenge can be edited on the challenges list page", async({page})=>{
        await apiservice.createNewChallenge(createChallengeRequest);
        await apiservice.deleteChallengeBySequenceNumber(editedChallengeCorrectDetails.SEQUENCE_NUMBER)
        await challengespage.gotoChallengesPage();
        await challengespage.turnOnEditChallenge(createChallengeRequest.body.sortNumber);
        await challengespage.fillInputField(challengespage.challengeSequenceNumberField, editedChallengeCorrectDetails.SEQUENCE_NUMBER)
        await challengespage.fillInputField(challengespage.challengeNameField, editedChallengeCorrectDetails.NAME)
        await challengespage.fillInputField(challengespage.challengeTitleField, editedChallengeCorrectDetails.TITLE)
        await challengespage.confirmEditChallenge();
        await challengespage.verifyElementExistance(
            challengespage.allChallengesSequenceNumbers,
            editedChallengeCorrectDetails.SEQUENCE_NUMBER,
            true
        );
    })

    test("Verify that an existing challenge can be edited", async ({ page }) => {
        addchallengepage = new AddChallengePage(page);
        await apiservice.deleteChallengeBySequenceNumber(editedChallengeCorrectDetails.SEQUENCE_NUMBER);
        await apiservice.createNewChallenge(createChallengeRequest);
        await challengespage.gotoChallengesPage();
        await challengespage.openChallengeInfoPage(createChallengeRequest.body.sortNumber);
        await addchallengepage.fillInputField(addchallengepage.challengeSequenceNumberField, editedChallengeCorrectDetails.SEQUENCE_NUMBER)
        await addchallengepage.fillInputField(addchallengepage.challengeNameField, editedChallengeCorrectDetails.NAME)
        await addchallengepage.fillInputField(addchallengepage.challengeTitleField, editedChallengeCorrectDetails.TITLE)
        await addchallengepage.fillInputField(addchallengepage.challengeDescriptionField, editedChallengeCorrectDetails.DESCRIPTION)
        await addchallengepage.confirmChallengeCreation();
        await addchallengepage.verifyElementVisibility(addchallengepage.challengeAddedSuccessMessage);
        await challengespage.gotoChallengesPage();
        await challengespage.verifyElementExistance(challengespage.allChallengesSequenceNumbers, editedChallengeCorrectDetails.SEQUENCE_NUMBER, true);
    });
    
    test("Verify that a task can be succesfully deleted", async({page})=>{
        taskspage = new TasksPage(page);
        await apiservice.createNewChallenge(createChallengeRequest);
        await apiservice.createNewTask(createTaskRequest);
        await taskspage.gotoTasksPage();
        await taskspage.deleteTask(createTaskRequest.body.name);
        await taskspage.verifyElementVisibility(taskspage.actionSuccessMessage);
        
        /*Verification below won't work if there a couple of tasks with the same name*/
        await taskspage.verifyElementExistance(
            taskspage.allTasksNames,
            createTaskRequest.body.name, false
        );
    })

    test("Verify that an existing task can be edited on the tasks list page", async({page})=>{
        taskspage = new TasksPage(page);
        await apiservice.createNewChallenge(createChallengeRequest);
        await apiservice.deleteTaskByName(editedTaskCorrectDetails.NAME)
        await apiservice.createNewTask(createTaskRequest);
        await taskspage.gotoTasksPage();
        await taskspage.turnOnEditTask(createTaskRequest.body.name);
        await taskspage.fillInputField(taskspage.taskNameField, editedTaskCorrectDetails.NAME)
        await taskspage.confirmEditTask();
        await taskspage.verifyElementExistance(
            challengespage.allChallengesSequenceNumbers,
            editedTaskCorrectDetails.NAME,
            true
        );
     })


    test("Verify that an existing task can be edited", async ({ page }) => {
        taskspage = new TasksPage(page);
        addtaskpage = new AddTaskPage(page);
        await apiservice.deleteTaskByName(editedTaskCorrectDetails.NAME);
        await apiservice.createNewChallenge(createChallengeRequest);
        await apiservice.createNewTask(createTaskRequest);
        await taskspage.gotoTasksPage();
        await taskspage.openTaskInfoPage(createTaskRequest.body.name);
        await addtaskpage.fillInputField(addtaskpage.taskNameField, editedTaskCorrectDetails.NAME);
        await addtaskpage.fillInputField(addtaskpage.taskTitleField, editedTaskCorrectDetails.TITLE);
        await addtaskpage.fillInputField(addtaskpage.taskDescriptionField, editedTaskCorrectDetails.DESCRIPTION);
        await addtaskpage.confirmTaskCreation();
        await addtaskpage.verifyElementVisibility(addtaskpage.taskAddedSuccessMessage);
        await taskspage.gotoTasksPage();
        await taskspage.verifyElementExistance(taskspage.allTasksNames, editedTaskCorrectDetails.NAME, true);
    });


    // test("Verify that the user can navigate through challenge's existing tasks", async({page})=>{
    //     await apiservice.createNewChallenge();
    // })


    test.afterEach(async({page})=>{
        await page.close();
    })
