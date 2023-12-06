package com.softserve.teachua.utils.test;

public final class Messages {
    public static final String NO_TITLE_MESSAGE = "There is no %s  with title '%s'.";
    public static final String NO_ID_MESSAGE = "There is no %s  with id '%s'.";
    public static final String NO_CORRECT_ANSWERS_MESSAGE = "There must be at least one correct answer.";
    public static final String NO_SUBSCRIPTION_MESSAGE = "There's no subscription with user id '%d' and group id '%d'.";
    public static final String INCORRECT_ENROLLMENT_KEY_MESSAGE = "Enrollment key '%s' is incorrect.";
    public static final String INCORRECT_MESSAGE = "Your answer is incorrect.";
    public static final String CORRECT_MESSAGE = "Your answer is correct.";
    public static final String PARTIALLY_CORRECT_MESSAGE = "Your answer is partially correct.";
    public static final String QUESTION_IS_NULL_MESSAGE = "Question in questionTest is null.";
    public static final String TEST_IS_NULL_MESSAGE = "Test in questionTest is null.";
    public static final String SUBSCRIPTION_EXISTS_MESSAGE = "'%s %s' already has a subscription.";
    public static final String TEST_WITHOUT_GROUP_MESSAGE = "The test with id %d does not belong to any of the groups";
    public static final String CATEGORY_EXISTS_WITH_TITLE = "Category with title %s already exists.";
    public static final String CATEGORY_CAN_NOT_BE_DELETED = "There are still answers in this category.";
    public static final String TYPE_CAN_NOT_BE_DELETED = "There are still answers in this type.";

    private Messages() {
    }
}
