package com.softserve.teachua.service.impl;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Complaint;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ComplaintRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.ArchiveService;
import com.softserve.teachua.service.ComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ComplaintServiceImpl implements ComplaintService {
    private static final String COMPLAINT_NOT_FOUND_BY_ID = "Complaint not found by id: %s";
    private static final String COMPLAINT_DELETING_ERROR = "Can't delete complaint cause of relationship";

    private final ComplaintRepository complaintRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private  final UserRepository userRepository;

    @Autowired
    public ComplaintServiceImpl(ComplaintRepository complaintRepository, DtoConverter dtoConverter, ClubRepository clubRepository, ArchiveService archiveService, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
        this.archiveService = archiveService;
        this.userRepository = userRepository;
    }

    /**
     * Method to find {@link Complaint}, and convert it to object of DTO class
     *
     * @param id - complaint id.
     * @return new {@code ComplaintResponse}
     * @throws NotExistException if complaint not exists.
     **/
    @Override
    public ComplaintResponse getComplaintProfileById(Long id) {
        return dtoConverter.convertToDto(getComplaintById(id), ComplaintResponse.class);
    }

    @Override
    public Complaint getComplaintById(Long id) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(id);
        if (!optionalComplaint.isPresent()) {
            throw new NotExistException(String.format(COMPLAINT_NOT_FOUND_BY_ID, id));
        }

        Complaint complaint = optionalComplaint.get();
        log.info("get complaint by id {} ", complaint);
        return complaint;
    }

    /**
     * Method add and save new {@link Complaint}
     *
     * @param complaintProfile profile with new Complaint data
     * @return SuccessCreatedComplaint
     * @throws NotExistException if complaint not exists.
     **/
    @Override
    public SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile) {
        if(!clubRepository.existsById(complaintProfile.getClubId())){
            throw new NotExistException("Club with id "+complaintProfile.getClubId()+" doesn`t exist");
        }
        if (!userRepository.existsById(complaintProfile.getUserId())){
            throw new NotExistException("User with id "+complaintProfile.getUserId()+"doesn`t exist");
        }

        Complaint complaint = complaintRepository.save(dtoConverter.convertToEntity(complaintProfile, new Complaint()));
        log.info("add new complaint {}", complaint);
        return dtoConverter.convertToDto(complaint, SuccessCreatedComplaint.class);
    }

    /**
     * Method get all {@link Complaint}s
     *
     * @return new {@code List<ComplaintResponse>}
     * @throws NotExistException if complaint not exists.
     **/
    @Override
    public List<ComplaintResponse> getAll() {
        List<ComplaintResponse> complaintResponses = complaintRepository.findAll()
                .stream()
                .map(complaint -> (ComplaintResponse) dtoConverter.convertToDto(complaint, ComplaintResponse.class))
                .collect(Collectors.toList());

        log.info("get all complaints for club: {} ", complaintResponses);
        return complaintResponses;
    }

    /**
     * Method get all {@link Complaint}s for club by club id
     *
     * @param clubId - club id
     * @return new {@code List<ComplaintResponse>}
     * @throws NotExistException if complaint not exists.
     **/
    @Override
    public List<ComplaintResponse> getAllByClubId(Long clubId) {
        List<ComplaintResponse> complaintResponses = complaintRepository.getAllByClubId(clubId)
                .stream()
                .map(complaint -> (ComplaintResponse) dtoConverter.convertToDto(complaint, ComplaintResponse.class))
                .collect(Collectors.toList());

        log.info("get all complaints: {} ", complaintResponses);
        return complaintResponses;
    }

    /**
     * Method find {@link Complaint} by id, and update data
     *
     * @param id               - complaint id
     * @param complaintProfile profile with data for Complaint
     * @return ComplaintProfile
     * @throws NotExistException if complaint not exists.
     **/
    @Override
    public ComplaintProfile updateComplaintProfileById(Long id, ComplaintProfile complaintProfile) {
        Complaint complaint = getComplaintById(id);
        Complaint newComplaint = dtoConverter.convertToEntity(complaintProfile, complaint).withId(id);

        complaintRepository.save(newComplaint);

        log.info("updated complaint {} ", newComplaint);
        return dtoConverter.convertToDto(newComplaint, ComplaintProfile.class);
    }

    /**
     * Method delete {@link Complaint}
     *
     * @param id - complaint id
     * @return new {@code ComplaintResponse}
     * @throws NotExistException if complaint not exists.
     **/
    @Override
    public ComplaintResponse deleteComplaintById(Long id) {
        Complaint complaint = getComplaintById(id);

        archiveService.saveModel(complaint);

        try {
            complaintRepository.deleteById(id);
            complaintRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(COMPLAINT_DELETING_ERROR);
        }

        log.info("complaint {} was successfully deleted", complaint);
        return dtoConverter.convertToDto(complaint, ComplaintResponse.class);
    }

}
