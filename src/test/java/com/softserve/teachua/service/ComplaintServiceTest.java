package com.softserve.teachua.service;


import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Complaint;
import com.softserve.teachua.model.Feedback;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.ComplaintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ComplaintServiceTest {
    @MockBean
    private ComplaintRepository complaintRepository;

    private final ComplaintService complaintService;

    private Complaint complaint1;
    private Complaint complaint2;

    private ComplaintProfile complaint1Profile;
    private ComplaintProfile complaint2Profile;

    private List<Complaint> complaintList;

    private static final String COMPLAINT_1_TEXT = "COMPLAINT_1_TEXT";
    private static final String COMPLAINT_2_TEXT = "COMPLAINT_2_TEXT";
    private static final String SOME_NOT_EXISTS_COMPLAINT_TEXT = "SOME NOT EXISTS COMPLAINT TEXT";

    private static final long COMPLAINT_1_ID = 1L;
    private static final long COMPLAINT_2_ID = 200L;
    private static final long SOME_NOT_EXISTS_COMPLAINT_ID = 500L;

    @Autowired
    public ComplaintServiceTest(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @BeforeEach
    void intiializeAndMockMethods() {
        complaint1 = new Complaint();
        complaint2 = new Complaint();

        complaint1Profile = new ComplaintProfile();
        complaint2Profile = new ComplaintProfile();

        complaintList = new ArrayList<>();
        complaintList.add(complaint1);
        complaintList.add(complaint2);

        complaint1.setText(COMPLAINT_1_TEXT);
        complaint1.setId(COMPLAINT_1_ID);

        complaint2.setText(COMPLAINT_2_TEXT);
        complaint2.setId(COMPLAINT_2_ID);

        complaint1Profile.setText(COMPLAINT_1_TEXT);
        complaint1Profile.setId(COMPLAINT_1_ID);

        complaint2Profile.setText(COMPLAINT_2_TEXT);
        complaint2Profile.setId(COMPLAINT_2_ID);

        Mockito.when(complaintRepository.findById(COMPLAINT_1_ID)).thenReturn(Optional.of(complaint1));
        Mockito.when(complaintRepository.findById(COMPLAINT_2_ID)).thenReturn(Optional.of(complaint2));
        Mockito.when(complaintRepository.findById(Mockito.argThat(arg -> arg != COMPLAINT_1_ID && arg != COMPLAINT_2_ID))).thenReturn(Optional.empty());

       /* Mockito.when(complaintRepository.findByName(COMPLAINT_1_TEXT)).thenReturn(Optional.of(complaint1));
        Mockito.when(complaintRepository.findByName(COMPLAINT_2_TEXT)).thenReturn(Optional.of(complaint2));
        Mockito.when(complaintRepository.findByName(Mockito.argThat(arg -> !arg.equals(COMPLAINT_1_TEXT) && !arg.equals(COMPLAINT_2_TEXT)))).thenReturn(Optional.empty());

        Mockito.when(complaintRepository.existsByName(COMPLAINT_1_TEXT)).thenReturn(true);
        Mockito.when(complaintRepository.existsByName(COMPLAINT_2_TEXT)).thenReturn(true);
        Mockito.when(complaintRepository.existsByName(Mockito.argThat(arg -> !arg.equals(COMPLAINT_1_TEXT) && !arg.equals(COMPLAINT_2_TEXT)))).thenReturn(false);*/

        Mockito.when(complaintRepository.findAll()).thenReturn(complaintList);

        Mockito.when(complaintRepository.save(Mockito.any(Complaint.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

    }

    @Test
    void testGetComplaintById() {
        Complaint actualComplaint1 = complaintService.getComplaintById(COMPLAINT_1_ID);
        Complaint actualComplaint2 = complaintService.getComplaintById(COMPLAINT_2_ID);

        assertThat(actualComplaint1.getId()).isEqualTo(COMPLAINT_1_ID);
        assertThat(actualComplaint2.getId()).isEqualTo(COMPLAINT_2_ID);

        assertThat(actualComplaint1.getText()).isEqualTo(COMPLAINT_1_TEXT);
        assertThat(actualComplaint2.getText()).isEqualTo(COMPLAINT_2_TEXT);

        assertThat(actualComplaint1).isEqualTo(complaint1);
        assertThat(actualComplaint2).isEqualTo(complaint2);

        assertThatThrownBy(() -> {
            complaintService.getComplaintById(SOME_NOT_EXISTS_COMPLAINT_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void testGetComplaintByProfileId() {
        ComplaintResponse complaint1Response = complaintService.getComplaintProfileById(COMPLAINT_1_ID);
        ComplaintResponse complaint2Response = complaintService.getComplaintProfileById(COMPLAINT_2_ID);

        assertThat(complaint1Response.getId()).isEqualTo(COMPLAINT_1_ID);
        assertThat(complaint2Response.getId()).isEqualTo(COMPLAINT_2_ID);

        assertThat(complaint1Response.getText()).isEqualTo(COMPLAINT_1_TEXT);
        assertThat(complaint2Response.getText()).isEqualTo(COMPLAINT_2_TEXT);

        assertThatThrownBy(() -> {
            complaintService.getComplaintProfileById(SOME_NOT_EXISTS_COMPLAINT_ID);
        }).isInstanceOf(NotExistException.class);
    }
    @Test
    void testAddComplaint() {

        ComplaintProfile newComplaint = ComplaintProfile.builder()
                .text(SOME_NOT_EXISTS_COMPLAINT_TEXT)
                .id(SOME_NOT_EXISTS_COMPLAINT_ID)
                .userId(1L)
                .build();

        SuccessCreatedComplaint createdComplaint = complaintService.addComplaint(newComplaint);


        assertThat(createdComplaint.getId()).isEqualTo(SOME_NOT_EXISTS_COMPLAINT_ID);

        assertThat(createdComplaint.getText()).isEqualTo(SOME_NOT_EXISTS_COMPLAINT_TEXT);

    }

    @Test
    void testGetAll() {
        List<ComplaintResponse> complaintResponseList = complaintService.getAll();

        Set<Complaint> actualComplaints = complaintResponseList.stream().map(r -> {
            Complaint complaint = new Complaint();
            complaint.setText(r.getText());
            complaint.setId(r.getId());

            return complaint;
        }).collect(Collectors.toSet());

        Set<Complaint> expectedComplaints = new HashSet<>(complaintList);

        assertThat(actualComplaints.size()).isEqualTo(expectedComplaints.size());

        assertThat(actualComplaints).isEqualTo(expectedComplaints);
    }

    @Test
    void testUpdateComplaintProfileById() {
        final String NEW_TEXT_1 = "New name 1";
        final String NEW_TEXT_2 = "New name 2";

        ComplaintProfile newComplaintProfile1 = new ComplaintProfile();
        ComplaintProfile newComplaintProfile2 = new ComplaintProfile();

        newComplaintProfile1.setText(NEW_TEXT_1);
        newComplaintProfile2.setText(NEW_TEXT_2);

        newComplaintProfile1.setId(COMPLAINT_1_ID);
        newComplaintProfile2.setId(COMPLAINT_2_ID);

        ComplaintProfile updatedProfile1 = complaintService.updateComplaintProfileById(COMPLAINT_1_ID, newComplaintProfile1);
        ComplaintProfile updatedProfile2 = complaintService.updateComplaintProfileById(COMPLAINT_2_ID, newComplaintProfile2);

        assertThat(updatedProfile1.getText()).isEqualTo(NEW_TEXT_1);
        assertThat(updatedProfile2.getText()).isEqualTo(NEW_TEXT_2);

        assertThat(updatedProfile1.getId()).isEqualTo(COMPLAINT_1_ID);
        assertThat(updatedProfile2.getId()).isEqualTo(COMPLAINT_2_ID);

        assertThat(updatedProfile1).isEqualTo(newComplaintProfile1);
        assertThat(updatedProfile2).isEqualTo(newComplaintProfile2);

        assertThatThrownBy(() -> {
            complaintService.updateComplaintProfileById(SOME_NOT_EXISTS_COMPLAINT_ID, newComplaintProfile1);
        }).isInstanceOf(NotExistException.class);
    }

    /*
    To Test:
        + ComplaintResponse getComplaintByProfileId(Long id);
        + Complaint getComplaintById(Long id);
        + Complaint getComplaintByName(String name);
        + SuccessCreatedComplaint addComplaint(ComplaintProfile complaintProfile);
        + List<ComplaintResponse> getAll();
        + ComplaintProfile updateComplaint(Long id, ComplaintProfile complaintProfile);
     */
}
