## Pull Request template

Please, go through these steps before you submit a PR.

1. Make sure that your PR fulfills these requirements:

    a. You have done your changes in a separate branch. Branches MUST have descriptive names.

    b. You have a descriptive commit message with a short title (first line).

    c. You have only one commit (if not, squash them into one commit).

    d. `mvn verify` doesn't throw any error. If it does, fix them first and amend your commit (`git commit --amend`).

2. **After** these steps, you're ready to open a pull request.

    a. Your pull request MUST NOT target the `main` branch on this repository. You probably want to target `rest` instead.

    b. Give a descriptive title to your PR.

    c. Provide a description of your changes.

    d. Put `closes #XXXX` in your comment to auto-close the issue that your PR fixes (if such).

**PLEASE REMOVE THIS TEMPLATE BEFORE SUBMITTING**

## Checklist:

-   [ ] I have added tests with code coverage `>=75%`
-   [ ] My code pass Checkstyle rules
-   [ ] PR is reviewed manually again (to make sure you have 100% ready code)
-   [ ] All reviewers agreed to merge the PR
