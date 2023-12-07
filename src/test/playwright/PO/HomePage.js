import { ADMIN_EMAIL, ADMIN_PASSWORD, USER_EMAIL, USER_PASSWORD, CITIES } from "../constants/general.constants";
import {API_URL} from "../constants/api.constants";
import {ADD_CLUB_PAGE, HOME_PAGE} from "../constants/locatorsText.constants";
import { SUCCESS_LOGIN_MESSAGE } from "../constants/messages.constants.js";
import BasePage from "./BasePage";

class HomePage extends BasePage{
    constructor(page){
        super(page);
        this.citiesDropdown = page.locator('.ant-dropdown-trigger.city');
        this.userDropdown = page.locator('.ant-dropdown-trigger.user-profile');
        this.registerButton = page.getByRole('menuitem', { name: HOME_PAGE.logIn });
        this.emailField = page.locator('input#basic_email');
        this.passwordField = page.locator('input#basic_password');
        this.loginButton = page.locator('button.login-button');
        this.loginSuccessMessage = page.locator('div.ant-message-success');
        this.addClubButton = this.page.getByRole('button', { name: ADD_CLUB_PAGE.addClub });
    }

    async gotoHomepage(){
        await this.page.goto(API_URL)
    }

    async openAddClubPage() {
        await this.page.goto(API_URL);
        await this.addClubButton.click();
    }

    /**
     * Performs a user interface login based on the specified user type.
     * @param {string} userType - The type of user to log in as (e.g., "admin" or "user").
     */
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
        await this.expectElementToHaveText(this.loginSuccessMessage, SUCCESS_LOGIN_MESSAGE);
    }
}

module.exports = HomePage;
