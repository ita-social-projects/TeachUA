package com.softserve.club.service;

import com.softserve.club.dto.complaint.ComplaintProfile;
import com.softserve.club.dto.complaint.ComplaintResponse;
import com.softserve.club.dto.complaint.SuccessCreatedComplaint;
import com.softserve.club.model.Complaint;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.marker.Archiver;
import java.util.List;

/**
 * This interface contains all needed methods to manage complaints.
 */

public interface ComplaintService extends Archiver<Long> {
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
    SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile);

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
