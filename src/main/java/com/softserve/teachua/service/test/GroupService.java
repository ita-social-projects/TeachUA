package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Group;

public interface GroupService {
    Group findByEnrollmentKey(String enrollmentKey);
}
