package com.softserve.teachua.service;

import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.model.Complaint;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ComplaintService {
    ComplaintResponse getComplaintProfileById(Long id);

    Complaint getComplaintById(Long id);

    SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile, HttpServletRequest httpServletRequest);

    List<ComplaintResponse> getAll();

    List<ComplaintResponse> getAllByClubId(Long clubId);

    ComplaintProfile updateComplaintProfileById(Long id, ComplaintProfile complaintProfile);

    ComplaintResponse deleteComplaintById(Long id);
}
