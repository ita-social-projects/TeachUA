package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.child.ChildProfile;
import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Child;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ChildRepository;
import com.softserve.teachua.service.impl.ChildServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChildServiceImplTest {
    @Mock
    private ChildRepository childRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private UserService userService;

    @InjectMocks
    private ChildServiceImpl childService;

    @Test
    void create() {
        ChildProfile childProfile = new ChildProfile();
        User user = new User();
        Child child = new Child();
        ChildResponse childResponse = new ChildResponse();

        when(userService.getAuthenticatedUserWithChildren()).thenReturn(user);
        when(dtoConverter.convertToEntity(any(ChildProfile.class), any(Child.class))).thenReturn(child);
        when(childRepository.save(child)).thenReturn(child);
        when(dtoConverter.convertToDto(any(Child.class), eq(ChildResponse.class))).thenReturn(childResponse);

        assertEquals(childResponse, childService.create(childProfile));

        verify(userService).getAuthenticatedUserWithChildren();
        verify(dtoConverter).convertToEntity(any(ChildProfile.class), any(Child.class));
        verify(childRepository).save(child);
        verify(dtoConverter).convertToDto(any(Child.class), eq(ChildResponse.class));
    }

    @Test
    void getAllByParentId() {
        Child child1 = new Child();
        Child child2 = new Child();
        ChildResponse childResponse1 = new ChildResponse();
        ChildResponse childResponse2 = new ChildResponse();

        List<Child> children = Arrays.asList(child1, child2);
        List<ChildResponse> childResponses = Arrays.asList(childResponse1, childResponse2);

        when(childRepository.getAllByParentId(1L)).thenReturn(children);
        when(dtoConverter.convertToDto(any(Child.class), any(ChildResponse.class))).thenReturn(childResponse1, childResponse2);
        assertEquals(childResponses, childService.getAllByParentId(1L));

        verify(childRepository).getAllByParentId(1L);
        verify(dtoConverter, times(2)).convertToDto(any(Child.class), any(ChildResponse.class));
    }

    @Test
    void getById() {
        Child child = new Child();
        child.setId(1L);

        when(childRepository.findById(1L)).thenReturn(Optional.of(child));

        assertEquals(child, childService.getById(1L));

        verify(childRepository).findById(1L);
    }

    @Test
    void getById_NotExist() {
        when(childRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotExistException.class, () -> childService.getById(1L));

        verify(childRepository).findById(1L);
    }
}
