import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD } from "../constants/general.constants";
import {API_URL} from "../constants/api.constants";
import {ADD_CLUB_PAGE} from "../constants/locatorsText.constants";
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
        this.addClubButton = this.page.getByRole("button", { name: ADD_CLUB_PAGE.addClub });
        this.harkivItem = page.getByRole('menuitem', { name: 'Харків' })
    }

    async gotoHomepage(){
        await this.page.goto(API_URL)
    }

    async openAddClubPage() {
        await this.page.goto(API_URL);
        await this.addClubButton.click();
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
        await this.expectElementToHaveText(this.loginSuccessMessage, successLoginMessage);
    }
}

module.exports = HomePage;
