import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {signInUrl} from "../constants/api.constants";

class BasePage {
    constructor(page) {
        this.page = page;
    }

    async apiLoginAs(userType) {
        const userData = {
            admin: { email: ADMIN_EMAIL, password: ADMIN_PASSWORD },
            user: { email: USER_EMAIL, password: USER_PASSWORD },
        };

        if(!userData[userType]){throw new Error('Invalid user type: ' + userType)}

        const { email, password } = userData[userType];

        const response = await fetch(signInUrl, {
            method: "POST",
            body: JSON.stringify({email, password}),
            headers: { "Content-Type": "application/json" },
        });

        let jsonResponse = await response.json();
        const { accessToken, id, roleName, refreshToken } = jsonResponse;
        
        await this.page.addInitScript(
            ({ id, accessToken, roleName, refreshToken}) => {
                window.localStorage.setItem("id", id);
                window.localStorage.setItem("accessToken", accessToken);
                window.localStorage.setItem("role", roleName);
                window.localStorage.setItem("refreshToken", refreshToken);
            },
            { id, accessToken, roleName, refreshToken }
        );
    }

    async elementHaveText(element, text) {
        await expect(element).toHaveText(text);
    }

    async elementToBeVisible(element, isVisible) {
        if (isVisible === true) {
            await expect(element).toBeVisible();
        } else if (isVisible === false) {
            await expect(element).not.toBeVisible();
        }
    }
}

module.exports = BasePage;