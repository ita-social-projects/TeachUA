
import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {signInUrl, createClubRequest , usersClubs} from "../constants/api.constants";


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

        const response = await fetch(signInUrl, {
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

    async getTotalPages(apiEndpoint, userId) {
        try{
        const clubResponse = await fetch(`${apiEndpoint}/${userId}?page=0`);
        const responseJson = await clubResponse.json();
        return responseJson.totalPages;
        }catch(e){
            console.error("Error fetching total pages of clubs: ", e);
            throw new Error('Failed to fetch total pages of clubs');
        }
    }

    async getAllClubsOfUser() {
        try {
            const totalPages = await this.getTotalPages(usersClubs, this.userId);
            let allClubsContent = [];
            for (let i = 0; i < totalPages; i++) {
                const pageResponse = await fetch(`${usersClubs}/${this.userId}?page=${i}`);
                const pageJson = await pageResponse.json();
                allClubsContent = allClubsContent.concat(pageJson.content);
            }
            return allClubsContent;
        } catch (e) {
            console.error("Error fetching clubs: ", e);
            throw new Error('Failed to fetch clubs');
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

    async createNewClub() {
        const response = await fetch(createClubRequest .url, {
            method: createClubRequest .method,
            body: JSON.stringify(createClubRequest .body),
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
        if (!response.ok) {
            console.log(await response.json());
            throw new Error("Request failed, club was not created \n");
        }
    }
}

module.exports = ApiService;