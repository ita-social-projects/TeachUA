package com.softserve.commons.user;

import com.softserve.commons.constant.RoleData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "user",
        url = "http://${APIGW_NETWORK}:${APIGW_USER_PORT}",
        path = "api/v1/user")
public interface UserClient {
    @GetMapping("/existsById/{id}")
    boolean existsById(@PathVariable("id") Long id);

    @GetMapping("/role/{userId}")
    RoleData getUserRoleByUserId(@PathVariable("userId") Long id);
}
