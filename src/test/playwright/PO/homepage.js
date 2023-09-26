import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {apiUrl} from "../constants/api.constants";
import { successLoginMessage } from "../constants/messages.constants.js";
import BasePage from "./BasePage";

class HomePage extends BasePage{
    constructor(page){
        super(page);
        this.citiesDropdown = page.locator('.ant-dropdown-trigger.city');
        this.userDropdown = page.locator('.ant-dropdown-trigger.user-profile');
        this.registerButton = page.getByRole('menuitem', { name: 'Увійти' });
        this.emailField = page.locator('input#basic_email');
        this.passwordField = page.locator('input#basic_password');
        this.loginButton = page.locator('button.login-button');
        this.loginSuccessMessage = page.locator('div.ant-message-success span:nth-child(2)');
        this.harkivItem = page.getByRole('menuitem', { name: 'Харків' })
    }

    async gotoHomepage(){
        await this.page.goto(apiUrl)
    }

    async uiLoginAs(userType){
        const userData = {
            admin: { email: ADMIN_EMAIL, password: ADMIN_PASSWORD },
            user: { email: USER_EMAIL, password: USER_PASSWORD },
        };
        await this.userDropdown.click();
        await this.registerButton.click();

        if (!userData[userType]) {
            throw new Error("Invalid user type: " + userType);
        }

        await this.emailField.fill(userData[userType].email);
        await this.passwordField.fill(userData[userType].password);

        await this.loginButton.click();
        await this.elementHaveText(this.loginSuccessMessage, successLoginMessage);
    }
}

module.exports = HomePage;
