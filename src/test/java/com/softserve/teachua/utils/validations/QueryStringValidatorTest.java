package com.softserve.teachua.utils.validations;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class QueryStringValidatorTest {

    private QueryStringValidator queryStringValidator;

    @BeforeEach
    public void setUp() {
        queryStringValidator = new QueryStringValidator();
    }

    @Test
    public void testValidQueryString() {
        String validQueryString = "Valid Query String";
        long elemCount = 10;

        assertTrue(queryStringValidator.isValid(validQueryString, elemCount));
    }

    @Test
    public void testInvalidEmptyQueryString() {
        String invalidQueryString = "";
        long elemCount = 10;

        assertFalse(queryStringValidator.isValid(invalidQueryString, elemCount));
    }

    @Test
    public void testInvalidNullQueryString() {
        String invalidQueryString = null;
        long elemCount = 10;

        assertFalse(queryStringValidator.isValid(invalidQueryString, elemCount));
    }

    @Test
    public void testInvalidShortQueryString() {
        String invalidQueryString = "ABC";
        long elemCount = 10;

        assertFalse(queryStringValidator.isValid(invalidQueryString, elemCount));
    }

    @Test
    public void testInvalidLongQueryString() {
        String invalidQueryString = "This is a very long query string exceeding the maximum allowed length of characters"; // More than MAX_QUERY_LENGTH
        long elemCount = 10;

        assertFalse(queryStringValidator.isValid(invalidQueryString, elemCount));
    }

    @Test
    public void testInvalidSpecialCharactersQueryString() {
        String invalidQueryString = "Invalid@Query*String";
        long elemCount = 10;

        assertFalse(queryStringValidator.isValid(invalidQueryString, elemCount));
    }

    @Test
    public void testInvalidNegativeElemCount() {
        String validQueryString = "Valid Query String";
        long invalidElemCount = -5;

        assertFalse(queryStringValidator.isValid(validQueryString, invalidElemCount));
    }
}
