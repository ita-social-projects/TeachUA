package com.softserve.teachua.controller.test;

import com.softserve.teachua.constants.RoleData;
import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.result.GoogleFormsInformation;
import com.softserve.teachua.dto.test.result.GoogleFormsWrapper;
import com.softserve.teachua.service.test.impl.GoogleFormsServiceImpl;
import com.softserve.teachua.utils.annotation.AllowedRoles;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for using Google Forms API.
 */

@RestController
public class GoogleFormsController implements Api {
    private final GoogleFormsServiceImpl formsService;

    public GoogleFormsController(GoogleFormsServiceImpl formsService) {
        this.formsService = formsService;
    }

    /**
     * Use this endpoint to get results from Google Forms document.
     *
     * @param formId the Google Forms ID which is retrieved from URL.
     * @return the form information with user quiz results.
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/google-forms/responses/{formId}")
    public GoogleFormsWrapper getUsersResults(@PathVariable String formId) {
        return formsService.getResultsFromGoogleForms(formId);
    }

    /**
     * Use this endpoint to get information from Google Forms document.
     *
     * @param formId the Google Forms ID which is retrieved from URL
     * @return the form information
     */
    @AllowedRoles(RoleData.ADMIN)
    @GetMapping("/google-forms/info/{formId}")
    public GoogleFormsInformation getFormInfo(@PathVariable String formId) {
        return formsService.getFormInfoFromGoogleForms(formId);
    }

    /**
     * Use this endpoint to create results from Google Forms document.
     *
     * @param results the results to save
     */
    @AllowedRoles(RoleData.ADMIN)
    @ResponseStatus(value = CREATED)
    @PostMapping("/google-forms/import-responses")
    public void saveUserResultsFromGoogleForms(@RequestBody GoogleFormsWrapper results) {
        formsService.createGoogleFormsResponse(results);
    }
}
