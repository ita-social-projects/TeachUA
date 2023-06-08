package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.dto.test.subscription.SubscriptionProfile;
import com.softserve.teachua.dto.test.user.UserResponse;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.Subscription;
import com.softserve.teachua.repository.test.SubscriptionRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.impl.SubscriptionServiceImpl;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {
    private final Long TEST_ID = 1L;
    private final Long TEST_WITHOUT_GROUP_ID = 10L;
    private final Long EXISTING_USER_ID = 1L;
    private final Long EXISTING_GROUP_ID = 1L;
    private final Long NOT_EXISTING_USER_ID = 100L;
    private final Long NOT_EXISTING_GROUP_ID = 100L;
    private final LocalDate expirationDate = LocalDate.now();
    private final ModelMapper mapper = new ModelMapper();
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private GroupService groupService;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;
    private Subscription subscription;
    private SubscriptionProfile subscriptionProfile;
    private CreateSubscription createSubscription;
    private Group group;
    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        group = generateGroupWithId(EXISTING_GROUP_ID);
        user = generateUser();
        subscription = generateSubscription();
        subscriptionProfile = generateSubscriptionProfile(subscription);
        createSubscription = generateCreateSubscription();
        userResponse = mapper.map(user, UserResponse.class);
    }

    @Test
    void findSubscriptionByExistingUserIdAndExistingGroupIdShouldReturnSubscription() {
        when(subscriptionRepository.findByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID))
                .thenReturn(subscription);

        Subscription actual = subscriptionService.findByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID);
        assertEquals(subscription, actual);
    }

    @Test
    void findSubscriptionByNotExistingUserIdAndExistingGroupIdShouldReturnNull() {
        when(subscriptionRepository.findByUserIdAndGroupId(NOT_EXISTING_USER_ID, EXISTING_GROUP_ID))
                .thenReturn(null);
        assertNull(subscriptionService.findByUserIdAndGroupId(NOT_EXISTING_USER_ID, EXISTING_GROUP_ID));
    }

    @Test
    void findSubscriptionByExistingUserIdAndNotExistingGroupIdShouldReturnNull() {
        when(subscriptionRepository.findByUserIdAndGroupId(EXISTING_USER_ID, NOT_EXISTING_GROUP_ID))
                .thenReturn(null);
        assertNull(subscriptionService.findByUserIdAndGroupId(EXISTING_USER_ID, NOT_EXISTING_GROUP_ID));
    }

    @Test
    void findSubscriptionByNullAndNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> subscriptionService.findByUserIdAndGroupId(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSubscriptionByTestIdAndEnrollmentKeyShouldReturnSubscriptionProfile() {
        when(groupService.findAllByTestId(TEST_ID)).thenReturn(Collections.singletonList(group));
        when(userService.getAuthenticatedUser()).thenReturn(user);

        SubscriptionProfile actual = subscriptionService.createSubscriptionByTestId(createSubscription, TEST_ID);
        assertEquals(subscriptionProfile, actual);
    }

    @Test
    void createSubscriptionThatAlreadyExistsByTestIdAndEnrollmentKeyShouldThrowIllegalStateException() {
        when(subscriptionRepository.findByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID))
                .thenReturn(subscription);
        when(groupService.findAllByTestId(TEST_ID)).thenReturn(Collections.singletonList(group));
        when(userService.getAuthenticatedUser()).thenReturn(user);
        assertThatThrownBy(() -> subscriptionService.createSubscriptionByTestId(createSubscription, TEST_ID))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void createSubscriptionBySubscriptionDtoIsNullAndTestIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> subscriptionService.createSubscriptionByTestId(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSubscriptionByTestIdAndIncorrectEnrollmentKeyShouldThrowIllegalArgumentException() {
        createSubscription.setEnrollmentKey("setIncorrectEnrollmentKey");
        when(groupService.findAllByTestId(TEST_ID)).thenReturn(Collections.singletonList(group));
        assertThatThrownBy(() -> subscriptionService.createSubscriptionByTestId(createSubscription, TEST_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSubscriptionByTestWithoutGroupIdAndEnrollmentKeyShouldThrowNoSuchElementException() {
        when(groupService.findAllByTestId(TEST_WITHOUT_GROUP_ID)).thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> subscriptionService
                .createSubscriptionByTestId(createSubscription, TEST_WITHOUT_GROUP_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void createSubscriptionByUserIdAndGroupIdShouldReturnSubscriptionProfile() {
        when(userService.getUserById(EXISTING_USER_ID)).thenReturn(user);
        when(groupService.findGroupById(EXISTING_GROUP_ID)).thenReturn(group);

        SubscriptionProfile actual = subscriptionService
                .createSubscriptionByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID);
        assertEquals(subscriptionProfile, actual);
    }

    @Test
    void createSubscriptionThatAlreadyExistsByUserIdAndGroupIdShouldThrowIllegalStateException() {
        when(userService.getUserById(EXISTING_USER_ID)).thenReturn(user);
        when(groupService.findGroupById(EXISTING_GROUP_ID)).thenReturn(group);
        when(subscriptionRepository.findByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID))
                .thenReturn(subscription);
        assertThatThrownBy(() -> subscriptionService
                .createSubscriptionByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void createSubscriptionByUserIdIsNullAndGroupIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> subscriptionService.createSubscriptionByUserIdAndGroupId(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createSubscriptionByNotExistingUserIdAndGroupIdShouldThrowNotExistException() {
        when(userService.getUserById(NOT_EXISTING_USER_ID)).thenThrow(new NotExistException());
        assertThatThrownBy(() -> subscriptionService
                .createSubscriptionByUserIdAndGroupId(NOT_EXISTING_USER_ID, EXISTING_GROUP_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void createSubscriptionByUserIdAndNotExistingGroupIdShouldThrowNotExistException() {
        when(userService.getUserById(EXISTING_USER_ID)).thenReturn(user);
        when(groupService.findGroupById(NOT_EXISTING_GROUP_ID)).thenThrow(new NotExistException());
        assertThatThrownBy(() -> subscriptionService
                .createSubscriptionByUserIdAndGroupId(EXISTING_USER_ID, NOT_EXISTING_GROUP_ID))
                .isInstanceOf(NotExistException.class);
    }

    @Test
    void deleteSubscriptionByUserIdAndGroupIdShouldReturnSubscriptionProfile() {
        when(subscriptionRepository.findAllByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID))
                .thenReturn(Collections.singletonList(subscription));

        SubscriptionProfile actual = subscriptionService
                .deleteSubscriptionByUserIdAndGroupId(EXISTING_USER_ID, EXISTING_GROUP_ID);
        assertEquals(subscriptionProfile, actual);
    }

    @Test
    void deleteSubscriptionByUserNotExistingIdAndGroupIdShouldThrowNoSuchElementException() {
        when(subscriptionRepository.findAllByUserIdAndGroupId(NOT_EXISTING_USER_ID, EXISTING_GROUP_ID))
                .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> subscriptionService
                .deleteSubscriptionByUserIdAndGroupId(NOT_EXISTING_USER_ID, EXISTING_GROUP_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteSubscriptionByUserIdAndGroupNotExistingIdShouldThrowNoSuchElementException() {
        when(subscriptionRepository.findAllByUserIdAndGroupId(EXISTING_USER_ID, NOT_EXISTING_GROUP_ID))
                .thenReturn(Collections.emptyList());
        assertThatThrownBy(() -> subscriptionService
                .deleteSubscriptionByUserIdAndGroupId(EXISTING_USER_ID, NOT_EXISTING_GROUP_ID))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteSubscriptionByUserIdIsNullAndGroupIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> subscriptionService
                .deleteSubscriptionByUserIdAndGroupId(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getUserResponsesByGroupIdShouldReturnListOfUserResponses() {
        when(subscriptionRepository.findAllByGroupId(EXISTING_GROUP_ID))
                .thenReturn(Collections.singletonList(subscription));
        when(modelMapper.map(user, UserResponse.class)).thenReturn(userResponse);

        List<UserResponse> actual = subscriptionService.getUserResponsesByGroupId(EXISTING_GROUP_ID);
        assertEquals(userResponse, actual.get(0));
    }

    @Test
    void getUserResponsesByGroupIdIsNullShouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> subscriptionService.getUserResponsesByGroupId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Subscription generateSubscription() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        subscription.setGroup(group);
        subscription.setUser(user);
        subscription.setExpirationDate(expirationDate);
        return subscription;
    }

    private Group generateGroupWithId(Long id) {
        Group group = new Group();
        group.setId(id);
        group.setTitle(String.format("groupTitle %d", id));
        group.setEnrollmentKey(String.format("enrollmentKey %d", id));
        group.setStartDate(LocalDate.of(2022, 1, 31));
        group.setEndDate(expirationDate);
        return group;
    }

    private SubscriptionProfile generateSubscriptionProfile(Subscription subscription) {
        SubscriptionProfile subscriptionProfile = new SubscriptionProfile();
        subscriptionProfile.setExpirationDate(subscription.getExpirationDate());
        subscriptionProfile.setUsername(subscription.getUser().getFirstName());
        subscriptionProfile.setGroupTitle(subscription.getGroup().getTitle());
        return subscriptionProfile;
    }

    private CreateSubscription generateCreateSubscription() {
        CreateSubscription createSubscription = new CreateSubscription();
        createSubscription.setEnrollmentKey(group.getEnrollmentKey());
        return createSubscription;
    }

    private User generateUser() {
        User user = new User();
        user.setFirstName("Michael");
        user.setLastName("Zubko");
        user.setId(EXISTING_USER_ID);
        return user;
    }
}
