package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.test.*;
import com.softserve.teachua.model.test.Test;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This interface contains all methods needed to manage tests.
 */
public interface TestService {
    /**
     * This method returns dto {@code SuccessCreatedTest} if test was successfully added.
     * @param test - put body of dto {@code CreateTest}.
     * @return new {@code SuccessCreatedTest}.
     */
    SuccessCreatedTest addTest(CreateTest test);

    /**
     * This method returns list of all active tests.
     * @return new {@code List<Test>}.
     */
    List<Test> findActiveTests();

    /**
     * This method returns list of all archived tests.
     * @return new {@code List<Test>}.
     */
    List<Test> findArchivedTests();

    /**
     * This method returns list of all tests by specific group.
     * @param groupId - put group id here.
     * @return new {@code List<Test>}
     */
    List<Test> findAllByGroupId(Long groupId);

    /**
     * This method returns list of all unarchived tests.
     * @return new {@code List<Test>}.
     */
    List<Test> findUnarchivedTests();

    /**
     * This method returns entity {@code Test} of test by id.
     * @param id - put test id.
     * @return new {@code Test}.
     * @throws NoSuchElementException if test does not exist.
     */
    Test findById(Long id);

    /**
     * This method archives test by id.
     * @param id - put test id.
     * @throws NoSuchElementException if test does not exist.
     * @return new {@code TestProfile}
     */
    TestProfile archiveTestById(Long id);

    /**
     * This method restores test by id.
     * @param id - put test id.
     * @throws NoSuchElementException if test does not exist.
     * @return new {@code TestProfile}
     */
    TestProfile restoreTestById(Long id);

    /**
     * This method returns dto {@code PassTest} by test id.
     * @param id - put test id.
     * @return new {@code PassTest}.
     * @throws NoSuchElementException if test does not exist.
     */
    PassTest findPassTestById(Long id);

    /**
     * This method returns list of dto {@code List<TestProfile>} of all unarchived tests.
     * @return new {@code List<TestProfile>}.
     */
    List<TestProfile> findUnarchivedTestProfiles();

    /**
     * This method returns list of dto {@code List<TestProfile>} of all archived tests.
     * @return new {@code List<TestProfile>}.
     */
    List<TestProfile> findArchivedTestProfiles();

    /**
     * This method returns dto {@code ViewTest} by test id.
     * @param id - put test id.
     * @return new {@code ViewTest}.
     */
    ViewTest findViewTestById(Long id);

    /**
     * This method returns true or false depending on whether the user has a subscription to the group
     * which contains a specific test.
     * @param userId - put user id here.
     * @param testId put test id here.
     * @return new {@code boolean}
     * @throws IllegalArgumentException if the parameter is null.
     */
    boolean hasActiveSubscription(Long userId, Long testId);
}
