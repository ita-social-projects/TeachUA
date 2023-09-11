import { expect} from "@playwright/test";

class BasePage {
  constructor(page) {
    this.page = page;
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