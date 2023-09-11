import { expect} from "@playwright/test";
import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {apiUrl} from "../constants/api.constants";
import { successLoginMessage } from "../constants/messages.constants.js";
import BasePage from "../PO/basepage";

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

    async uiLoginAs(role){
        await this.userDropdown.click();
        await this.registerButton.click();
        if(role === 'admin'){
            await this.emailField.fill(ADMIN_EMAIL)
            await this.passwordField.fill(ADMIN_PASSWORD)
        } else if (role === 'user'){
            await this.emailField.fill(USER_EMAIL)
            await this.passwordField.fill(USER_PASSWORD)
        }
        await this.loginButton.click();
        await this.elementHaveText(this.loginSuccessMessage, successLoginMessage);
    }

    async clickOnDropdown(){
        await this.citiesDropdown.click();
        await this.harkivItem.click();
    }
}

module.exports = HomePage;
