import { expect} from "@playwright/test";
import { apiUrl, adminLogin, adminPassword, userLogin, userPassword } from "../constants/general";
import { successLoginMessage } from "../constants/messages";
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

    async loginAs(role){
        await this.userDropdown.click();
        await this.registerButton.click();
        if(role === 'admin'){
            await this.emailField.fill(adminLogin)
            await this.passwordField.fill(adminPassword)
        } else if (role === 'user'){
            await this.emailField.fill(userLogin)
            await this.passwordField.fill(userPassword)
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
