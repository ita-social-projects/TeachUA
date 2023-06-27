package com.softserve.teachua.service;

import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.SuccessCreatedChild;
import jakarta.validation.Valid;

public interface ChildService {
    SuccessCreatedChild create(@Valid ChildProfile childProfile);
}
