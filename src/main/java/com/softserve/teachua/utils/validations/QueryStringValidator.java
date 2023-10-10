package com.softserve.teachua.utils.validations;

import org.springframework.stereotype.Component;

@Component
public class QueryStringValidator {
    private static final int MIN_QUERY_LENGTH = 4;
    private static final int MAX_QUERY_LENGTH = 50;

    public boolean isValid(String queryString, long elemCount) {
        if (queryString == null || queryString.trim().isEmpty()) {
            return false;
        }

        if (elemCount < 0) {
            return false;
        }

        if (queryString.length() < MIN_QUERY_LENGTH || queryString.length() > MAX_QUERY_LENGTH) {
            return false;
        }

        if (!queryString.matches("^[a-zA-Z0-9\\s]+$")) {
            return false;
        }

        return true;
    }
}
