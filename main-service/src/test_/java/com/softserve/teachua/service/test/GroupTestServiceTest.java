package com.softserve.teachua.service.test;

import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.GroupTest;
import com.softserve.teachua.repository.test.GroupTestRepository;
import com.softserve.question.service.impl.GroupTestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupTestServiceTest {

    @Mock
    GroupTestRepository groupTestRepository;

    @Mock
    GroupService groupService;

    @Mock
    TestService testService;

    @InjectMocks
    GroupTestServiceImpl groupTestService;

    private Group group;
    private com.softserve.teachua.model.test.Test test;
    private GroupTest groupTest;

    private final Long EXISTING_TEST_ID = 1L;
    private final Long EXISTING_GROUP_ID = 1L;

    @BeforeEach
    public void setUp() {
        group = Group.builder()
                .id(EXISTING_GROUP_ID)
                .build();
        test = com.softserve.teachua.model.test.Test.builder()
                .id(EXISTING_TEST_ID)
                .build();
        groupTest = GroupTest.builder()
                .test(test)
                .group(group)
                .build();
    }

    @Test
    void saveGroupTestWithNullGroupIdShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> groupTestService.addTestToGroup(null, EXISTING_TEST_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void saveGroupTestWithNullTestIdShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> groupTestService.addTestToGroup(EXISTING_GROUP_ID, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void saveGroupTestShouldReturnGroupTest() {
        when(groupService.findGroupById(EXISTING_GROUP_ID)).thenReturn(group);
        when(testService.findById(EXISTING_TEST_ID)).thenReturn(test);
        when(groupTestRepository.save(groupTest)).thenReturn(groupTest);

        GroupTest actual = groupTestService.addTestToGroup(EXISTING_GROUP_ID, EXISTING_TEST_ID);
        assertEquals(groupTest, actual);
    }
}
