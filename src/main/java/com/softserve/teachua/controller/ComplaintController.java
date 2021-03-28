package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.service.ComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @return ComplaintResponse
     */
    @GetMapping("/complaint/{id}")
    public ComplaintResponse getComplaintById(@PathVariable Long id) {
        return complaintService.getComplaintProfileById(id);
    }

    /**
     * The method to get all Complaints
     *
     * @return List of ComplaintResponse {@code List<ComplaintResponse>}
     */
    @GetMapping("/complaints")
    public List<ComplaintResponse> getAllComplaints() {
        return complaintService.getAll();
    }

    /**
     * The method to get all Complaints by club id
     *
     * @return List of ComplaintResponse
     */
    @GetMapping("/complaints/club/{id}")
    public List<ComplaintResponse> getAllComplaints(@PathVariable Long id) {
        return complaintService.getAllByClubId(id);
    }

    /**
     * The method to create a new Complaint
     *
     * @param complaintProfile - object of DTO class
     * @return SuccessCreatedComplaint
     */
    @PostMapping("/complaint")
    public SuccessCreatedComplaint addComplaint(@RequestBody ComplaintProfile complaintProfile) {
        return complaintService.addComplaint(complaintProfile);
    }

    /**
     * The method to update Complaint
     *
     * @param id               Complaint id
     * @param complaintProfile Complaint profile with new data
     * @return ComplaintProfile
     */
    @PutMapping("/complaint/{id}")
    public ComplaintProfile updateComplaint(@PathVariable Long id, @RequestBody ComplaintProfile complaintProfile) {
        return complaintService.updateComplaintProfileById(id, complaintProfile);
    }

    /**
     * The method to delete Complaint
     *
     * @param id id of Complaint to be deleted
     * @return ComplaintResponse
     */
    @DeleteMapping("/complaint/{id}")
    public ComplaintResponse deleteComplaintById(@PathVariable Long id) {
        return complaintService.deleteComplaintById(id);
    }

}
