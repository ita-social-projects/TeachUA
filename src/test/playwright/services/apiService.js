
import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {signInUrl, clubCreation} from "../constants/api.constants";


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

    async getAllUsersClubs() {
        const clubResponse = await fetch(`http://localhost:8080/dev/api/clubs/1`);
        return clubResponse.json();
    }

    async deleteClubById(id) {
        await fetch(`http://localhost:8080/dev/api/club/${id}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` },
        });
    }

    async deleteClubByTitle(title) {
        const clubs = await this.getAllUsersClubs();
        const club = clubs.content.find((c) => c.name === title);
        
        if (!club) {
            throw new Error("Club wasn't deleted as it doesn't exist (hasn't been created or the title is wrong");
        }

        await this.deleteClubById(club.id)
    }

    async createNewClub(){
       await fetch(clubCreation.url, {
            method: clubCreation.method,
            body: JSON.stringify(clubCreation.body),
            headers: { "Content-Type": "application/json", Authorization: `Bearer ${this.token}` }
        });
    }
}

module.exports = ApiService;