package com.softserve.teachua.service;

import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.ComplaintUpdateAnswer;
import com.softserve.teachua.dto.complaint.ComplaintUpdateIsActive;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Complaint;
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

    List<ComplaintResponse> getAllByUserId(Long userId, boolean isSender);

    /**
     * Method get all {@link Complaint}s by recipient id.
     *
     * @param recipientId - recipient id
     * @return new {@code List<ComplaintResponse>}
     * @throws NotExistException if complaint not exists.
     */
    List<ComplaintResponse> getAllByRecipientId(Long recipientId);

    /**
     * Method get all {@link Complaint}s by sender id.
     *
     * @param senderId - sender id
     * @return new {@code List<ComplaintResponse>}
     * @throws NotExistException if complaint not exists.
     */
    List<ComplaintResponse> getAllBySenderId(Long senderId);

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
     * Method updates complaint isActive field and returns dto {@code ComplaintProfile} of updated complaint.
     *
     * @param id                      - complaint id
     * @param complaintUpdateIsActive - profile with data for Complaint
     * @return new {@code ComplaintProfile}
     * @throws NotExistException if complaint not exists.
     */
    ComplaintResponse updateComplaintIsActive(Long id, ComplaintUpdateIsActive complaintUpdateIsActive);

    /**
     * Method updates complaint answerText field and returns dto {@code ComplaintProfile} of updated complaint.
     *
     * @param id                    - complaint id
     * @param updateComplaintAnswer - profile with data for Complaint
     * @return new {@code ComplaintProfile}
     * @throws NotExistException if complaint not exists.
     */
    ComplaintResponse updateComplaintAnswer(Long id, ComplaintUpdateAnswer updateComplaintAnswer);

    /**
     * Method deletes complaint and returns dto {@code ComplaintResponse} of deleted complaint.
     *
     * @param id - complaint id
     * @return new {@code ComplaintResponse}
     * @throws NotExistException if complaint not exists.
     */
    ComplaintResponse deleteComplaintById(Long id);
}
