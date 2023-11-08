
import BasePage from "./BasePage";

class ChallengeInfoPage extends BasePage {
    constructor(page) {
        super(page);
        this.clubPageTitle = page.locator("div.name-box");
        this.clubPageTags = page.locator("span.tag span.name");
        this.clubPageAddress = page.locator("div.address");
        this.clubPageDescription = page.locator("div.content");
        this.clubPageContactData = page.locator("div.social-media div.links").nth(0);//.locator('span.contact-name');
        this.clubPageDevelopmentCenter = page.locator("div.center span.name");
    }
}

module.exports = ChallengeInfoPage;