package com.softserve.club.controller;

import com.softserve.club.controller.marker.Api;
import com.softserve.club.dto.complaint.ComplaintProfile;
import com.softserve.club.dto.complaint.ComplaintResponse;
import com.softserve.club.dto.complaint.SuccessCreatedComplaint;
import com.softserve.club.service.ComplaintService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing the complaints.
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/club/complaint")
public class ComplaintController implements Api {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * Use this endpoint to get Complaint by id. The controller returns {@code ComplaintResponse}.
     *
     * @param id
     *            - put complaint id here.
     *
     * @return {@link ComplaintResponse}
     */
    @GetMapping("/{id}")
    public ComplaintResponse getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintProfileById(id);
    }

    /**
     * Use this endpoint to get all Complaints. The controller returns {@code List<ComplaintResponse>}.
     *
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/all")
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAll();
    }

    /**
     * Use this endpoint to get all Complaints by club id The controller returns {@code List<ComplaintResponse>}.
     *
     * @param id
     *            - put club id here.
     *
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/all/club/{id}")
    public List<ComplaintResponse> getAllComplaints(@PathVariable Long id) {
        return complaintService.getAllByClubId(id);
    }

    /**
     * Use this endpoint to create a new Complaint The controller returns {@code SuccessCreatedComplaint}.
     *
     * @param complaintProfile
     *            - put complaint information here.
     *
     * @return new {@link SuccessCreatedComplaint}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public SuccessCreatedComplaint addComplaint(@Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.addComplaint(complaintProfile);
    }

    /**
     * Use this endpoint to update Complaint. The controller returns {@code ComplaintProfile}.
     *
     * @param id
     *            Complaint id
     * @param complaintProfile
     *            Complaint profile with new data
     *
     * @return {@code ComplaintProfile}.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ComplaintProfile updateComplaint(@PathVariable Long id,
            @Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.updateComplaintProfileById(id, complaintProfile);
    }

    /**
     * Use this endpoint to delete Complaint The controller returns {@code ComplaintResponse}.
     *
     * @param id
     *            - put complaint id here.
     *
     * @return {@link ComplaintResponse}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ComplaintResponse deleteComplaintById(@PathVariable Long id) {
        return complaintService.deleteComplaintById(id);
    }
}
