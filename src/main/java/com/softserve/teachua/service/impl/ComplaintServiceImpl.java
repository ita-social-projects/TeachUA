package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Complaint;
import com.softserve.teachua.model.archivable.ComplaintArch;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ComplaintRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ComplaintServiceImpl implements ComplaintService, ArchiveMark<Complaint> {
    private static final String COMPLAINT_NOT_FOUND_BY_ID = "Complaint not found by id: %s";
    private static final String COMPLAINT_DELETING_ERROR = "Can't delete complaint cause of relationship";

    private final ComplaintRepository complaintRepository;
    private final ClubRepository clubRepository;
    private final DtoConverter dtoConverter;
    private final ArchiveService archiveService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final ClubService clubService;

    @Autowired
    public ComplaintServiceImpl(ComplaintRepository complaintRepository, DtoConverter dtoConverter,
            ClubRepository clubRepository, ArchiveService archiveService, UserRepository userRepository,
            UserService userService, ObjectMapper objectMapper, ClubService clubService) {
        this.complaintRepository = complaintRepository;
        this.dtoConverter = dtoConverter;
        this.clubRepository = clubRepository;
        this.archiveService = archiveService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.clubService = clubService;
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
    public SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile,
            HttpServletRequest httpServletRequest) {
        complaintProfile.setUserId(userService.getUserFromRequest(httpServletRequest).getId());

        if (!clubRepository.existsById(complaintProfile.getClubId())) {
            throw new NotExistException("Club with id " + complaintProfile.getClubId() + " does`nt exists");
        }
        if (!userRepository.existsById(complaintProfile.getUserId())) {
            throw new NotExistException("User with id " + complaintProfile.getUserId() + "does`nt exists");
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
                .collect(Collectors.toList());

        log.debug("get all complaints for club: {} ", complaintResponses);
        return complaintResponses;
    }

    @Override
    public List<ComplaintResponse> getAllByClubId(Long clubId) {
        List<ComplaintResponse> complaintResponses = complaintRepository.getAllByClubId(clubId).stream()
                .map(complaint -> (ComplaintResponse) dtoConverter.convertToDto(complaint, ComplaintResponse.class))
                .collect(Collectors.toList());

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

        archiveModel(complaint);

        log.debug("complaint {} was successfully deleted", complaint);
        return dtoConverter.convertToDto(complaint, ComplaintResponse.class);
    }

    @Override
    public void archiveModel(Complaint complaint) {
        archiveService.saveModel(dtoConverter.convertToDto(complaint, ComplaintArch.class));
    }

    @Override
    public void restoreModel(String archiveObject) throws JsonProcessingException {
        ComplaintArch complaintArch = objectMapper.readValue(archiveObject, ComplaintArch.class);
        Complaint complaint = Complaint.builder().build();
        complaint = dtoConverter.convertToEntity(complaintArch, complaint).withId(null)
                .withClub(clubService.getClubById(complaintArch.getClubId()))
                .withUser(userService.getUserById(complaintArch.getUserId()));
        complaintRepository.save(complaint);
    }
}
