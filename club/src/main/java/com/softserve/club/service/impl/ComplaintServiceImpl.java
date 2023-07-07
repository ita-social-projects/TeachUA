package com.softserve.club.service.impl;

import com.softserve.club.dto.complaint.ComplaintProfile;
import com.softserve.club.dto.complaint.ComplaintResponse;
import com.softserve.club.dto.complaint.SuccessCreatedComplaint;
import com.softserve.club.model.Complaint;
import com.softserve.club.repository.ClubRepository;
import com.softserve.club.repository.ComplaintRepository;
import com.softserve.commons.security.UserPrincipal;
import com.softserve.club.service.ComplaintService;
import com.softserve.commons.exception.DatabaseRepositoryException;
import com.softserve.commons.exception.NotExistException;
import com.softserve.commons.util.converter.DtoConverter;
import jakarta.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ComplaintServiceImpl implements ComplaintService {
    private static final String COMPLAINT_NOT_FOUND_BY_ID = "Complaint not found by id: %s";
    private static final String COMPLAINT_DELETING_ERROR = "Can't delete complaint cause of relationship";

    private final ComplaintRepository complaintRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, DtoConverter dtoConverter,
                                ClubRepository clubRepository) {
        this.complaintRepository = complaintRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
    }

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

    @Override
    public SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile) {
        complaintProfile.setUserId(((UserPrincipal)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());

        if (!clubRepository.existsById(complaintProfile.getClubId())) {
            throw new NotExistException("Club with id " + complaintProfile.getClubId() + " does`nt exists");
        }

        Complaint complaint = complaintRepository
                .save(dtoConverter.convertToEntity(complaintProfile, new Complaint()).withDate(LocalDate.now()));

        log.debug("add new complaint {}", complaint);
        return dtoConverter.convertToDto(complaint, SuccessCreatedComplaint.class);
    }

    @Override
    public List<ComplaintResponse> getAll() {
        List<ComplaintResponse> complaintResponses = complaintRepository.findAll().stream()
                .map(complaint -> (ComplaintResponse) dtoConverter.convertToDto(complaint, ComplaintResponse.class))
                .toList();

        log.debug("get all complaints for club: {} ", complaintResponses);
        return complaintResponses;
    }

    @Override
    public List<ComplaintResponse> getAllByClubId(Long clubId) {
        List<ComplaintResponse> complaintResponses = complaintRepository.getAllByClubId(clubId).stream()
                .map(complaint -> (ComplaintResponse) dtoConverter.convertToDto(complaint, ComplaintResponse.class))
                .toList();

        log.debug("get all complaints: {} ", complaintResponses);
        return complaintResponses;
    }

    @Override
    public ComplaintProfile updateComplaintProfileById(Long id, ComplaintProfile complaintProfile) {
        Complaint complaint = getComplaintById(id);
        Complaint newComplaint = dtoConverter.convertToEntity(complaintProfile, complaint).withId(id);

        complaintRepository.save(newComplaint);

        log.debug("updated complaint {} ", newComplaint);
        return dtoConverter.convertToDto(newComplaint, ComplaintProfile.class);
    }

    @Override
    public ComplaintResponse deleteComplaintById(Long id) {
        Complaint complaint = getComplaintById(id);

        try {
            complaintRepository.deleteById(id);
            complaintRepository.flush();
        } catch (DataAccessException | ValidationException e) {
            throw new DatabaseRepositoryException(COMPLAINT_DELETING_ERROR);
        }

        //archiveModel(complaint);

        log.debug("complaint {} was successfully deleted", complaint);
        return dtoConverter.convertToDto(complaint, ComplaintResponse.class);
    }

    //todo@
    /*
    @Override
    public void archiveModel(Complaint complaint) {
        archiveService.saveModel(dtoConverter.convertToDto(complaint, ComplaintArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        ComplaintArch complaintArch = objectMapper.readValue(archiveObject, ComplaintArch.class);
        Complaint complaint = Complaint.builder().build();
        complaint = dtoConverter.convertToEntity(complaintArch, complaint).withId(null)
                .withClub(clubService.getClubById(complaintArch.getClubId()));
        //.withUser(userService.getUserById(complaintArch.getUserId()))

        complaintRepository.save(complaint);
    }
    */
}
