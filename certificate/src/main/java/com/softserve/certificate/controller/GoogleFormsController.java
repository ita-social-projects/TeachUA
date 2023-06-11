package com.softserve.certificate.controller;

import com.softserve.certificate.dto.googleapis.GoogleFormsResponse;
import com.softserve.certificate.service.GoogleFormsService;
import com.softserve.certificate.utils.annotation.AllowedRoles;
import com.softserve.commons.constant.RoleData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//todo

/**
 * This controller is for using Google Forms API.
 */
@RestController
@RequestMapping("/api/v1/google-forms")
public class GoogleFormsController {
    private final GoogleFormsService googleService;

    public GoogleFormsController(GoogleFormsService googleService) {
        this.googleService = googleService;
    }

    /**
     * Use this endpoint to get results from a Google Forms document.
     *
     * @param formId the Google Forms ID which is retrieved from URL.
     * @return the form information with user quiz results.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/responses/{formId}")
    public GoogleFormsResponse getResults(@PathVariable String formId) {
        return googleService.getResultsFromGoogleForms(formId);
    }
}
