package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.GroupTest;

/**
 * This interface contains all methods needed to manage relations between groups and tests.
 */
public interface GroupTestService {
    /**
     * This method adds new relation between groups and tests.
     * @param testId - put test id.
     * @param groupId - put group id.
     * @return new {@code GroupTest}
     */
    GroupTest addTestToGroup(Long testId, Long groupId);
}
