package com.softserve.teachua.utils.annotation;

import static com.softserve.teachua.TestUtils.getUser;
import static com.softserve.teachua.TestUtils.getUserPrincipal;
import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.exception.UserPermissionException;
import com.softserve.teachua.model.User;
import com.softserve.teachua.security.CustomUserDetailsService;
import com.softserve.teachua.security.UserPrincipal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AllowedRolesAspectTest {
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private ProceedingJoinPoint jp;
    @Mock
    private MethodSignature methodSignature;
    private AllowedRolesAspect allowedRolesAspect;
    private User user;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        allowedRolesAspect = new AllowedRolesAspect(customUserDetailsService);
        when(jp.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getMethod()).thenReturn(this.getClass().getMethod("methodForAdmin"));
        user = getUser();
    }

    @Test
    void givenRoleAdmin_thenProceed() throws Throwable {
        user.getRole().setName(RoleData.ADMIN.getDBRoleName());
        UserPrincipal userPrincipal = getUserPrincipal(user);
        when(customUserDetailsService.getUserPrincipal()).thenReturn(userPrincipal);

        allowedRolesAspect.doSomething(jp);
        verify(jp).proceed();
    }

    @Test
    void givenRoleUser_thenThrowUserPermissionException() {
        user.getRole().setName(RoleData.USER.getDBRoleName());
        UserPrincipal userPrincipal = getUserPrincipal(user);
        when(customUserDetailsService.getUserPrincipal()).thenReturn(userPrincipal);

        assertThrows(UserPermissionException.class, () -> allowedRolesAspect.doSomething(jp));
    }

    @AllowedRoles(RoleData.ADMIN)
    public void methodForAdmin() {
        // Dummy method to be used in the tests
    }
}
