package com.softserve.teachua.service;

import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Complaint;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This interface contains all needed methods to manage complaints.
 */

public interface ComplaintService {
    /**
     * Method to find {@link Complaint}, and convert it to object of DTO class.
     *
     * @param id - complaint id.
     * @return new {@code ComplaintResponse}
     * @throws NotExistException if complaint not exists.
     */
    ComplaintResponse getComplaintProfileById(Long id);

    /**
     * Method returns entity of {@code Complaint}.
     *
     * @param id - complaint id.
     * @return new {@code Complaint}
     * @throws NotExistException if complaint not exists.
     */
    Complaint getComplaintById(Long id);

    /**
     * Method add and save new entity {@link Complaint} and returns dto {@code SuccessCreatedComplaint}.
     *
     * @param complaintProfile - profile with new Complaint data
     * @return new {@code SuccessCreatedComplaint}
     * @throws NotExistException if complaint not exists.
     */
    SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile, HttpServletRequest httpServletRequest);

    /**
     * Method list of dto {@link ComplaintResponse} of all complaints.
     *
     * @return new {@code List<ComplaintResponse>}
     * @throws NotExistException if complaint not exists.
     */
    List<ComplaintResponse> getAll();

    /**
     * Method get all {@link Complaint}s for club by club id.
     *
     * @param clubId - club id
     * @return new {@code List<ComplaintResponse>}
     * @throws NotExistException if complaint not exists.
     */
    List<ComplaintResponse> getAllByClubId(Long clubId);

    /**
     * Method updates complaint and returns dto {@code ComplaintProfile} of updated complaint.
     *
     * @param id               - complaint id
     * @param complaintProfile - profile with data for Complaint
     * @return new {@code ComplaintProfile}
     * @throws NotExistException if complaint not exists.
     */
    ComplaintProfile updateComplaintProfileById(Long id, ComplaintProfile complaintProfile);

    /**
     * Method deletes complaint and returns dto {@code ComplaintResponse} of deleted complaint.
     *
     * @param id - complaint id
     * @return new {@code ComplaintResponse}
     * @throws NotExistException if complaint not exists.
     */
    ComplaintResponse deleteComplaintById(Long id);
}
