package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.GoogleFormsInformation;
import com.softserve.teachua.dto.test.result.GoogleFormsWrapper;

/**
 * This interface contains all methods needed to manage Google Forms responses.
 */
public interface GoogleFormsService {
    /**
     * Retrieve response results from Google Forms document by given form id.
     *
     * @param formId Google Forms ID is a unique string containing letters, numbers, and some special characters.
     * @return wrapper object which contains form information and quiz responses
     */
    GoogleFormsWrapper getResultsFromGoogleForms(String formId);

    /**
     * Retrieve information from Google Forms document by given form id.
     *
     * @param formId Google Forms ID is a unique string containing letters, numbers, and some special characters
     * @return form information object which contains form title and description
     */
    GoogleFormsInformation getFormInfoFromGoogleForms(String formId);

    /**
     * Persists quiz results to database.
     *
     * @param results wrapper object which contains form information and quiz responses
     */
    void createGoogleFormsResponse(GoogleFormsWrapper results);
}
