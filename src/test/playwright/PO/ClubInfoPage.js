
import BasePage from "./BasePage";
import { CLUB_INFO_PAGE } from "../constants/locatorsText.constants"

class ClubInfoPage extends BasePage {
    constructor(page) {
        super(page);
        this.clubPageTitle = page.locator('div.name-box');
        this.clubPageTags = page.locator('span.tag span.name');
        this.clubPageAddress = page.locator('div.address');
        this.clubPageDescription = page.locator('div.content');
        this.clubPageContactData = page.locator('div.social-media').filter({hasText: CLUB_INFO_PAGE.contactClub}).locator('div.links');
        this.clubPageDevelopmentCenter = page.locator('div.center span.name');
    }
}

module.exports = ClubInfoPage;