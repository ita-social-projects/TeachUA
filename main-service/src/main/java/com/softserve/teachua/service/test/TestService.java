package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.PassTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;
import com.softserve.teachua.dto.test.test.TestProfile;
import com.softserve.teachua.dto.test.test.ViewTest;
import com.softserve.teachua.model.test.Test;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This interface contains all methods needed to manage tests.
 */
public interface TestService {
    /**
     * Create a new test using the DTO {@code CreateTest}.
     *
     * @param test body of dto {@code CreateTest}.
     * @return dto {@code SuccessCreatedTest} if test was successfully added
     */
    SuccessCreatedTest addTest(CreateTest test);

    /**
     * Find all active tests.
     *
     * @return list of all active tests
     */
    List<Test> findActiveTests();

    /**
     * Find all archived tests.
     *
     * @return a list of all archived tests
     */
    List<Test> findArchivedTests();

    /**
     * Find all tests by a specific group.
     *
     * @param groupId group id
     * @return a list of all tests by a specific group.
     */
    List<Test> findAllByGroupId(Long groupId);

    /**
     * Find all unarchived tests.
     *
     * @return list of all unarchived tests
     */
    List<Test> findUnarchivedTests();

    /**
     * Find test entity by given test id.
     *
     * @param id test id
     * @return entity {@code Test} of test by id
     * @throws NoSuchElementException if a test does not exist.
     */
    Test findById(Long id);

    /**
     * This method archive test by id.
     *
     * @param id - put test id.
     * @return new {@code TestProfile}
     * @throws NoSuchElementException if a test does not exist.
     */
    TestProfile archiveTestById(Long id);

    /**
     * This method restores test by id.
     *
     * @param id - put test id.
     * @return new {@code TestProfile}
     * @throws NoSuchElementException if a test does not exist.
     */
    TestProfile restoreTestById(Long id);

    /**
     * Find pass test by given test id.
     *
     * @param id test id
     * @return dto {@code PassTest} by test id
     * @throws NoSuchElementException if a test does not exist.
     */
    PassTest findPassTestById(Long id);

    /**
     * Find all unarchived tests.
     *
     * @return a list of dto {@code List<TestProfile>} of all unarchived tests
     */
    List<TestProfile> findUnarchivedTestProfiles();

    /**
     * Find all archived tests.
     *
     * @return a list of dto {@code List<TestProfile>} of all archived tests
     */
    List<TestProfile> findArchivedTestProfiles();

    /**
     * Find test by given id.
     *
     * @param id test id
     * @return dto {@code ViewTest} by test id
     */
    ViewTest findViewTestById(Long id);

    /**
     * Discover whether the user has a subscription to the group which contains a specific test.
     *
     * @param userId user id
     * @param testId test id
     * @return new {@code boolean}
     * @throws IllegalArgumentException if the parameter is null.
     */
    boolean hasActiveSubscription(Long userId, Long testId);
}
