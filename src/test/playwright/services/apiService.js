
import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {signInUrl} from "../constants/api.constants";


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

    async receiveResponseOfGetRequest() {
        await page.waitForResponse('');

    }
}

module.exports = ApiService;