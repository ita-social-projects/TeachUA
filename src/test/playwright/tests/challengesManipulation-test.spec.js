import { test} from "@playwright/test";
import ApiService from "../services/apiService";
import ChallengesPage from "../PO/ChallengesPage";
import TasksPage from "../PO/TasksPage";
import AddChallengePage from "../PO/AddChallengePage";
import AddTaskPage from "../PO/AddTaskPage";
import {createChallengeRequest, tasksAdminUrl} from "../constants/api.constants";
import {newChallengeCorrectDetails, newTaskCorrectDetails} from "../constants/challengeAndTaskInformation.constants";


let apiservice, challengespage, taskspage, addchallengepage, addtaskpage;

    test.beforeEach(async({page})=>{
        apiservice = new ApiService(page);
        challengespage = new ChallengesPage(page);
        await apiservice.apiLoginAs('admin');
    })

    test("Verify that a new challenge can be successfully created", async ({ page }) => {
        await challengespage.gotoChallengesPage();
        addchallengepage = new AddChallengePage(page);
        try {
            await apiservice.deleteChallengeBySequenceNumber(newChallengeCorrectDetails.SEQUENCE_NUMBER)
        } catch (e) {
            console.error(e);
        }
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

        await apiservice.createNewChallenge();
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
        
    });


    test("Verify that a new task can be added to the existing challenge", async({page})=>{
        await apiservice.createNewChallenge();
    })



    // test("Verify that a challenge can be succesfully deleted", async({page})=>{
    //     await apiservice.createNewChallenge();
    // })
    // test("Verify that a task can be removed from the existing challenge", async({page})=>{
    //     await apiservice.createNewChallenge();
    // })
    // test("Verify that an existing challenge can be edited", async({page})=>{
    //     await apiservice.createNewChallenge();
    // })
    // test("Verify that an existing task can be edited", async({page})=>{
    //     await apiservice.createNewChallenge();
    // })
    // test("Verify that the user can navigate through challenge's existing tasks", async({page})=>{
    //     await apiservice.createNewChallenge();
    // })


    test.afterEach(async({page})=>{
       //await page.close();
    })
