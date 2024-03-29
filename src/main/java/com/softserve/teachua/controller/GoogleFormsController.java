package com.softserve.teachua.controller;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.googleapis.GoogleFormsResponse;
import com.softserve.teachua.service.GoogleFormsService;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for using Google Forms API.
 */
@RestController
public class GoogleFormsController implements Api {
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
    @GetMapping("/google-forms/responses/{formId}")
    public GoogleFormsResponse getResults(@PathVariable String formId) {
        return googleService.getResultsFromGoogleForms(formId);
    }
}
