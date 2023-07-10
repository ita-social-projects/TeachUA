package com.softserve.teachua.service.impl;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ChildRepository;
import com.softserve.teachua.service.ChildService;
import com.softserve.teachua.service.ClubRegistrationService;
import com.softserve.teachua.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {
    private final ChildRepository childRepository;
    private final DtoConverter dtoConverter;
    private final UserService userService;
    @Override
    public ChildResponse create(ChildProfile childProfile) {
        User user = userService.getAuthenticatedUserWithChildren();
        log.debug("Got user {}", user);
        Child child = dtoConverter.convertToEntity(childProfile, new Child());
        log.debug("Dto converted to entity {}", child);

        child.setParent(user);
        log.debug("Set parent {}", child);

        child = childRepository.save(child);

        return dtoConverter.convertToDto(child, ChildResponse.class);
    }

    @Override
    public Child getById(Long id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new NotExistException("Child has not found by id" + id));
    }

    @Override
    public List<ChildResponse> getAllByParentId(Long parentId) {
        List<Child> children = childRepository.getAllByParentId(parentId);
        ChildResponse cr = new ChildResponse();

        return children.stream()
                .map(c -> dtoConverter.convertToDto(c, cr))
                .toList();
    }

}
