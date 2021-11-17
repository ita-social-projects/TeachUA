package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.service.ComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
public class ComplaintController implements Api {

    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * The method to get Complaint by id
     *
     * @param id Complaint id
     * @return {@link ComplaintResponse}
     */
    @GetMapping("/complaint/{id}")
    public ComplaintResponse getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintProfileById(id);
    }

    /**
     * The method to get all Complaints
     *
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints")
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAll();
    }

    /**
     * The method to get all Complaints by club id
     *
     * @param id - club id
     * @return {@link List<ComplaintResponse>}
     */
    @GetMapping("/complaints/club/{id}")
    public List<ComplaintResponse> getAllComplaints(@PathVariable Long id) {
        return complaintService.getAllByClubId(id);
    }

    /**
     * The method to create a new Complaint
     *
     * @param complaintProfile - object of DTO class
     * @return new {@link SuccessCreatedComplaint}
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/complaint")
    public SuccessCreatedComplaint addComplaint(
            @Valid @RequestBody ComplaintProfile complaintProfile, HttpServletRequest httpServletRequest) {
        return complaintService.addComplaint(complaintProfile,httpServletRequest);
    }

    /**
     * The method to update Complaint
     *
     * @param id               Complaint id
     * @param complaintProfile Complaint profile with new data
     * @return {@link ComplaintProfile}
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/complaint/{id}")
    public ComplaintProfile updateComplaint(@PathVariable Long id,@Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.updateComplaintProfileById(id, complaintProfile);
    }

    /**
     * The method to delete Complaint
     *
     * @param id id of Complaint to be deleted
     * @return {@link ComplaintResponse}
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/complaint/{id}")
    public ComplaintResponse deleteComplaintById(@PathVariable Long id) {
        return complaintService.deleteComplaintById(id);
    }

}
