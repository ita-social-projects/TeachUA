package com.softserve.edu.testcases.testdata;

import com.softserve.edu.models.placeholder.login.UserLoginDto;

public class LoginTestData {

    public LoginTestData() {
        // Default constructor
    }

    public UserLoginDto signin() {
        return new UserLoginDto()
                .setEmail(System.getenv().get("ADMIN_LOGIN"))
                .setPassword(System.getenv().get("ADMIN_PASSWORD"));
    }

}
