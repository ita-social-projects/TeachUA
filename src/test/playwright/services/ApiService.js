
import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {SIGN_IN_URL, CREATE_CLUB_REQUEST , USERS_CLUBS, CREATE_CHALLENGE_REQUEST, CREATE_TASK_REQUEST, GET_CHALLENGES_REQUEST, GET_TASK_REQUEST} from "../constants/api.constants";


class ApiService {
    constructor(page) {
        this.page = page;
    }
    async apiLoginAs(userType) {
        const userData = {
            admin: { email: ADMIN_EMAIL, password: ADMIN_PASSWORD },
            user: { email: USER_EMAIL, password: USER_PASSWORD },
        };

        if (!userData[userType]) {
            throw new Error("Invalid user type: " + userType);
        }

        const { email, password } = userData[userType];

        const response = await fetch(SIGN_IN_URL, {
            method: "POST",
            body: JSON.stringify({ email, password }),
            headers: { "Content-Type": "application/json" },
        });

        await expect(response.status).toBe(200);

        let jsonResponse = await response.json();
        const { accessToken, id, roleName, refreshToken } = jsonResponse;
        this.token = accessToken;
        this.userId = id;

        await this.page.addInitScript(
            ({ id, accessToken, roleName, refreshToken }) => {
                window.localStorage.setItem("id", id);
                window.localStorage.setItem("accessToken", accessToken);
                window.localStorage.setItem("role", roleName);
                window.localStorage.setItem("refreshToken", refreshToken);
            },
            { id, accessToken, roleName, refreshToken }
        );
        await this.page.reload();
    }

    async assertResponseIsOk(response) {
        expect(response.ok, `Response should be OK. Status: ${response.status}`).toBeTruthy();
    }

    // Clubs interaction

    // Get the total pages of clubs and assert that the response is successful.
    async getTotalPages(apiEndpoint, userId) {
        const clubResponse = await fetch(`${apiEndpoint}/${userId}?page=0`);
        const responseJson = await clubResponse.json();
        await this.assertResponseIsOk(clubResponse);
        return responseJson.totalPages;
    }

    async createNewClub() {
        const response = await fetch(CREATE_CLUB_REQUEST.url, {
            method: CREATE_CLUB_REQUEST.method,
            body: JSON.stringify(CREATE_CLUB_REQUEST.body),
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        if ((await response.json()).status === 409) {
            console.log("Club already exist");
        } else if (!response.ok) {
            console.log(await response.json());
            throw new Error("Request failed, club was not created \n");
        }
    }

    async getAllClubsOfUser() {
        try {
            const totalPages = await this.getTotalPages(USERS_CLUBS, this.userId);
            let allClubsContent = [];
            for (let i = 0; i < totalPages; i++) {
                const pageResponse = await fetch(`${USERS_CLUBS}/${this.userId}?page=${i}`);
                const pageJson = await pageResponse.json();
                allClubsContent = allClubsContent.concat(pageJson.content);
            }
            return allClubsContent;
        } catch (e) {
            console.error("Error fetching clubs: ", e);
            throw new Error("Failed to fetch clubs");
        }
    }

    async deleteClubById(id) {
        const response = await fetch(`http://localhost:8080/dev/api/club/${id}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        if (!response.ok) {
            console.log(await response.json());
            throw new Error("Request failed, club was not deleted \n");
        }
    }

    async deleteClubByTitle(title) {
        const clubs = await this.getAllClubsOfUser();
        const club = clubs.find((c) => c.name === title);

        if (!club) {
            console.log("Club wasn't deleted as it doesn't exist (hasn't been created or the title is wrong)");
            return;
        }

        await this.deleteClubById(club.id);
    }

    // Challenges
    async createNewChallenge(CREATE_CHALLENGE_REQUEST) {
        const response = await fetch(CREATE_CHALLENGE_REQUEST.url, {
            method: CREATE_CHALLENGE_REQUEST.method,
            body: JSON.stringify(CREATE_CHALLENGE_REQUEST.body),
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        if ((await response.json()).status === 400) {
            console.log("Challenge already exist");
        }
    }

    async deleteChallengeBySequenceNumber(sequenceNumber) {
        const challenges = await this.getAllChallenges();
        const challenge = challenges.find((c) => c.sortNumber === parseInt(sequenceNumber));
        if (!challenge) {
            console.log("Challenge wasn't deleted as it doesn't exist (hasn't been created or the name is wrong)");
            return;
        }
        await this.deleteChallengeById(challenge.id);
    }

    async deleteChallengeById(id) {
        const response = await fetch(`http://localhost:8080/dev/api/challenge/${id}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        await this.assertResponseIsOk(response);
    }

    async getAllChallenges() {
        const pageResponse = await fetch(GET_CHALLENGES_REQUEST);
        await this.assertResponseIsOk(pageResponse);
        const pageJson = await pageResponse.json();
        return pageJson;
    }

    // Tasks

    /**
     * Create a new task associated with a challenge.
     * This function retrieves a list of challenges, finds the challenge with a specific
     * sortNumber, and creates a new task associated with that challenge.
     */
    async createNewTask(CREATE_TASK_REQUEST) {
        const tasks = await this.getAllTasks();
        const task = tasks.find((c) => c.name === CREATE_TASK_REQUEST.body.name);
        if (task) {
            console.log("Task already exist");
            return;
        }
        const challenges = await this.getAllChallenges();
        const challenge = challenges.find((c) => c.sortNumber === parseInt(CREATE_CHALLENGE_REQUEST.body.sortNumber));
        CREATE_TASK_REQUEST.body.challengeId = challenge.id;
        if (challenge) {
            const response = await fetch(`http://localhost:8080/dev/api/challenge/${challenge.id}/task`, {
                method: CREATE_TASK_REQUEST.method,
                body: JSON.stringify(CREATE_TASK_REQUEST.body),
                headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
            });
            await this.assertResponseIsOk(response);
        } else {
            console.error("Challenge is not found or sortNumber is invalid");
        }
    }

    async deleteTaskByName(taskName) {
        const tasks = await this.getAllTasks();
        const task = tasks.find((c) => c.name === taskName);
        if (!task) {
            console.log("Task wasn't deleted as it doesn't exist (hasn't been created or the name is wrong)");
            return;
        }
        await this.deleteTaskById(task.id);
    }

    async deleteTaskById(id) {
        const response = await fetch(`http://localhost:8080/dev/api/challenge/task/${id}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        await this.assertResponseIsOk(response);
    }

    // Helper function to get all tasks
    async getAllTasks() {
        const pageResponse = await fetch(GET_TASK_REQUEST, {
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        await this.assertResponseIsOk(pageResponse);
        const pageJson = await pageResponse.json();
        return pageJson;
    }

    // Helper function to get a task by name
    async getTaskByName(taskName) {
        const tasks = await this.getAllTasks();
        return tasks.find((c) => c.name === taskName);
    }

    // Helper function to fetch task details
    async fetchTaskDetails(taskId) {
        const response = await fetch(`http://localhost:8080/dev/api/challenge/task/${taskId}`, {
            method: "GET",
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        await this.assertResponseIsOk(response);
        return response.json();
    }

    // Helper function to update task details
    async updateTaskDetails(updatedTask) {
        const response = await fetch(`http://localhost:8080/dev/api/challenge/task/${updatedTask.id}`, {
            method: "PUT",
            body: JSON.stringify(updatedTask),
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        await this.assertResponseIsOk(response);
    }

    /**
     * Updates the start date of a task to today's date, 
     * ensuring it appears among the active tasks of the challenge.
     * @param {string} taskName - The name of the task to be updated.
     */
    async changeTaskDateToToday(taskName) {
        const task = await this.getTaskByName(taskName);

        if (!task) {
            console.log("Task wasn't updated as it doesn't exist (hasn't been created or the name is wrong)");
            return;
        }

        const pageJson = await this.fetchTaskDetails(task.id);

        const currentDate = new Date();
        pageJson.startDate = currentDate.toISOString().split("T")[0];

        await this.updateTaskDetails(pageJson);

        console.log(pageJson);
    }
}

module.exports = ApiService;
