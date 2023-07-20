package com.softserve.teachua.service;

import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.model.Child;
import jakarta.validation.Valid;
import java.util.List;

public interface ChildService {
    ChildResponse create(@Valid ChildProfile childProfile);

    Child getById(Long id);

    List<ChildResponse> getAllByParentId(Long parentId);
}
