import { expect} from "@playwright/test";
import {signInUrl} from "../constants/api.constants";

class BasePage {
    constructor(page) {
        this.page = page;
        this.navBarCityDropdown = page.locator('header > div.right-side-menu div.city')
        this.navBarCityDropdownList = page.locator('body > div:last-child ul')
    }

    async expectElementToHaveText(element, text) {
        await expect(element).toHaveText(text);
    }

    async expectElementToContainText(element, text) {
        await expect(element).toContainText(text);
    }

    async elementToBeVisible(element, isVisible) {
        if(!(typeof isVisible === 'boolean')){
          throw new Error('Second paramenter should be boolean');
        }
        if (isVisible === true) {
            await expect(element).toBeVisible();
        } else if (isVisible === false) {
            await expect(element).not.toBeVisible();
        }
    }

    async selectCityInNavBar(city){
        await this.navBarCityDropdown.click();
        await (await this.navBarCityDropdownList.getByText(city)).click();
    }

    async sortElementsAsc([...elements]) {
        return elements.sort();
    }

    async sortElementsDesc([...elements]) {
        return elements.sort((a, b) => b - a);
    }
}

module.exports = BasePage;