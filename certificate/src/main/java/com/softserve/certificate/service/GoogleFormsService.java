package com.softserve.certificate.service;

import com.softserve.certificate.dto.googleapis.GoogleFormsResponse;

/**
 * This interface contains all methods needed to manage Google Forms responses.
 */
public interface GoogleFormsService {
    /**
     * Retrieve response results from Google Forms document by given form id.
     *
     * @param formId Google Forms ID is a unique string containing letters, numbers, and some special characters.
     * @return object which contains form information and quiz responses
     */
    GoogleFormsResponse getResultsFromGoogleForms(String formId);
}
