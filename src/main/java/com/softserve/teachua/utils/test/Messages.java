package com.softserve.teachua.utils.test;

public interface Messages {
    String NO_TITLE_MESSAGE = "There is no %s  with title '%s'.";
    String NO_ID_MESSAGE = "There is no %s  with id '%s'.";
    String NO_CORRECT_ANSWERS_MESSAGE = "There must be at least one correct answer.";
    String NO_SUBSCRIPTION_MESSAGE = "There's no subscription with user id '%d' and group id '%d'.";
    String INCORRECT_ENROLLMENT_KEY_MESSAGE = "Enrollment key '%s' is incorrect.";
    String INCORRECT_MESSAGE = "Your answer is incorrect.";
    String CORRECT_MESSAGE = "Your answer is correct.";
    String PARTIALLY_CORRECT_MESSAGE = "Your answer is partially correct.";
    String QUESTION_IS_NULL_MESSAGE = "Question in questionTest is null.";
    String TEST_IS_NULL_MESSAGE = "Test in questionTest is null.";
    String SUBSCRIPTION_EXISTS_MESSAGE = "'%s %s' already has a subscription.";
    String TEST_WITHOUT_GROUP_MESSAGE = "The test with id %d does not belong to any of the groups";
    String GROUP_EXISTS_WITH_ENROLLMENT_KEY = "Group with enrollment key %s already exists.";
    String GROUP_EXISTS_WITH_TITLE = "Group with title %s already exists.";
    String CATEGORY_EXISTS_WITH_TITLE = "Category with title %s already exists.";
    String TYPE_EXISTS_WITH_TITLE = "Type with title %s already exists.";
    String CATEGORY_CAN_NOT_BE_DELETED = "There are still answers in this category.";
    String TYPE_CAN_NOT_BE_DELETED = "There are still answers in this type.";
    String TOPIC_EXISTS_WITH_TITLE = "Topic with title %s already exists.";
}
