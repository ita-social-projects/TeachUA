package com.softserve.teachua.service.impl;

import org.springframework.stereotype.Service;

@Service
public class BadService {

    public String getNoting() {
        String message;
        boolean flag;
        flag = false;
        if (flag) {
            message = "Empty String";
            if (message != null)
                message = "Not null";
        }

        message = "String with some information";
        return message;
    }
}
