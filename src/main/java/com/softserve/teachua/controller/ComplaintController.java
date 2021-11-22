package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.service.ComplaintService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@Tag(name="complaint", description="the Complaint API")
@SecurityRequirement(name = "api")
public class ComplaintController implements Api {

    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    /**
     * Use this endpoint to get Complaint by id.
     * The controller returns {@code ComplaintResponse}.
     * @param id - put complaint id here.
     * @return {@link ComplaintResponse}
     */
    @GetMapping("/complaint/{id}")
    public ComplaintResponse getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintProfileById(id);
    }

    /**
     * Use this endpoint to get all Complaints.
     * The controller returns {@code List<ComplaintResponse>}.
     * @return {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints")
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAll();
    }

    /**
     * Use this endpoint to get all Complaints by club id
     * The controller returns {@code List<ComplaintResponse>}.
     * @param id - put club id here.
     * @return {@link List<ComplaintResponse>}
     */
    @GetMapping("/complaints/club/{id}")
    public List<ComplaintResponse> getAllComplaints(@PathVariable Long id) {
        return complaintService.getAllByClubId(id);
    }

    /**
     * Use this endpoint to create a new Complaint
     * The controller returns {@code SuccessCreatedComplaint}.
     * @param complaintProfile - put complaint information here.
     * @return new {@link SuccessCreatedComplaint}
     */
    @PostMapping("/complaint")
    public SuccessCreatedComplaint addComplaint(
            @Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.addComplaint(complaintProfile);
    }

    /**
     * Use this endpoint to update Complaint.
     * The controller returns {@code ComplaintProfile}.
     * @param id               Complaint id
     * @param complaintProfile Complaint profile with new data
     * @return {@link ComplaintProfile}
     */
    @PutMapping("/complaint/{id}")
    public ComplaintProfile updateComplaint(@PathVariable Long id,@Valid @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.updateComplaintProfileById(id, complaintProfile);
    }

    /**
     * Use this endpoint to delete Complaint
     * The controller returns {@code ComplaintResponse}.
     * @param id - put complaint id here.
     * @return {@link ComplaintResponse}
     */
    @DeleteMapping("/complaint/{id}")
    public ComplaintResponse deleteComplaintById(@PathVariable Long id) {
        return complaintService.deleteComplaintById(id);
    }

}
