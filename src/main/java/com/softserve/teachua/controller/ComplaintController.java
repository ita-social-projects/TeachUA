package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.ComplaintUpdateAnswer;
import com.softserve.teachua.dto.complaint.ComplaintUpdateIsActive;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.service.ComplaintService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the complaints.
 */

@Slf4j
@RestController
@Tag(name = "complaint", description = "the Complaint API")
@SecurityRequirement(name = "api")
public class ComplaintController implements Api {
    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * Use this endpoint to get Complaint by id. The controller returns {@code ComplaintResponse}.
     *
     * @param id - put complaint id here.
     * @return {@link ComplaintResponse}
     */
    @GetMapping("/complaint/{id}")
    public ComplaintResponse getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintProfileById(id);
    }

    /**
     * Use this endpoint to get all Complaints. The controller returns {@code List<ComplaintResponse>}.
     *
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints")
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAll();
    }

    /**
     * Use this endpoint to get all Complaints by club id The controller returns {@code List<ComplaintResponse>}.
     *
     * @param id - put club id here.
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints/club/{id}")
    public List<ComplaintResponse> getAllComplaints(@PathVariable Long id) {
        return complaintService.getAllByClubId(id);
    }

    /**
     * Use this endpoint to get all Complaints by recipient id The controller returns {@code List<ComplaintResponse>}.
     *
     * @param id - put recipient id here.
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints/recipient/{id}")
    public List<ComplaintResponse> getAllComplaintsByRecipientId(@PathVariable Long id) {
        return complaintService.getAllByUserId(id, false);
    }

    /**
     * Use this endpoint to get all Complaints by sender id The controller returns {@code List<ComplaintResponse>}.
     *
     * @param id - put sender id here.
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints/sender/{id}")
    public List<ComplaintResponse> getAllComplaintsBySenderId(@PathVariable Long id) {
        return complaintService.getAllByUserId(id, true);
    }

    /**
     * Use this endpoint to create a new Complaint The controller returns {@code SuccessCreatedComplaint}.
     *
     * @param complaintProfile - put complaint information here.
     * @return new {@link SuccessCreatedComplaint}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/complaint")
    public SuccessCreatedComplaint addComplaint(@Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.addComplaint(complaintProfile);
    }

    /**
     * Use this endpoint to update Complaint. The controller returns {@code ComplaintProfile}.
     *
     * @param id               Complaint id
     * @param complaintProfile Complaint profile with new data
     * @return {@code ComplaintProfile}.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/complaint/{id}")
    public ComplaintProfile updateComplaint(@PathVariable Long id,
                                            @Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.updateComplaintProfileById(id, complaintProfile);
    }

    /**
     * Use this endpoint to update Complaint isActive status. The controller returns {@code ComplaintResponse}.
     *
     * @param id                      Complaint id
     * @param complaintUpdateIsActive Complaint profile with new data
     * @return {@code ComplaintResponse}.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/complaint/isActive/{id}")
    public ComplaintResponse updateComplaintIsActive(@PathVariable Long id,
                                                     @RequestBody ComplaintUpdateIsActive complaintUpdateIsActive) {
        return complaintService.updateComplaintIsActive(id, complaintUpdateIsActive);
    }

    /**
     * Use this endpoint to update Complaint add answer The controller returns {@code ComplaintResponse}.
     *
     * @param id                    Complaint id
     * @param complaintUpdateAnswer Complaint profile with new data
     * @return {@code ComplaintResponse}.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/complaint/{id}/answer")
    public ComplaintResponse updateComplaintAnswer(@PathVariable Long id,
                                                   @RequestBody ComplaintUpdateAnswer complaintUpdateAnswer) {
        return complaintService.updateComplaintAnswer(id, complaintUpdateAnswer);
    }

    /**
     * Use this endpoint to delete Complaint The controller returns {@code ComplaintResponse}.
     *
     * @param id - put complaint id here.
     * @return {@link ComplaintResponse}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/complaint/{id}")
    public ComplaintResponse deleteComplaintById(@PathVariable Long id) {
        return complaintService.deleteComplaintById(id);
    }
}
