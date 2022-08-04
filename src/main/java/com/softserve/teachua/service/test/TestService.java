package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
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
     */
    void archiveTestById(Long id);

    /**
     * This method returns dto {@code ResultTest} by test id and result id.
     * @param testId - put test id.
     * @param resultId - put result id.
     * @return new {@code ResultTest}.
     */
    ResultTest getResultTest(Long testId, Long resultId);

    /**
     * This method returns dto {@code SuccessCreatedResult} if result was successfully added.
     * @param resultDto - put body of dto {@code CreateResult}.
     * @return new {@code SuccessCreatedResult}.
     */
    SuccessCreatedResult saveResult(CreateResult resultDto);

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
     * This method returns dto {@code ViewTest} by test id.
     * @param id - put test id.
     * @return new {@code ViewTest}.
     */
    ViewTest findViewTestById(Long id);
}
