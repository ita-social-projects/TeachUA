package com.softserve.teachua.dto.test.user;

import com.softserve.teachua.dto.test.result.UserResult;
import lombok.Data;

import java.util.List;

@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private List<UserResult> userResults;
}
