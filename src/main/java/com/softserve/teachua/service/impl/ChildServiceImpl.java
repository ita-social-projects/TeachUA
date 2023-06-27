package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.SuccessCreatedChild;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ChildRepository;
import com.softserve.teachua.service.ChildService;
import com.softserve.teachua.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final UserService userService;
    private final DtoConverter dtoConverter;
    private final ChildRepository childRepository;
    @Override
    public SuccessCreatedChild create(ChildProfile childProfile) {
        User user = userService.getAuthenticatedUserWithChildren();
        log.debug("Got user {}", user);
        Child child = dtoConverter.convertToEntity(childProfile, new Child());
        log.debug("Dto converted to entity {}", child);

        child.setParent(user);
        log.debug("Set parent {}", child);

        child = childRepository.save(child);

        return dtoConverter.convertToDto(child, SuccessCreatedChild.class);
    }
}
