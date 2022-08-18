package com.softserve.teachua.service;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.complaint.ComplaintProfile;
import com.softserve.teachua.dto.complaint.ComplaintResponse;
import com.softserve.teachua.dto.complaint.SuccessCreatedComplaint;
import com.softserve.teachua.exception.DatabaseRepositoryException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Archive;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Complaint;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.archivable.ClubArch;
import com.softserve.teachua.model.archivable.ComplaintArch;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.repository.ComplaintRepository;
import com.softserve.teachua.repository.UserRepository;
import com.softserve.teachua.service.impl.ComplaintServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import  static  org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ComplaintServiceTest {

    private  static  final  Long CORRECT_COMPLAINT_ID = 1l;
    private  static  final  Long WRONG_COMPLAINT_ID = 2l;
    private  static  final  Long CLUB_ID = 100l;
    private  static  final  Long USER_ID =  1000l;

    @Mock
    private ComplaintRepository complaintRepository;
    @Mock
    private DtoConverter dtoConverter;
    @Mock
    private ArchiveService archiveService;
    @Mock
    private HttpServletRequestWrapper httpServletRequest;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ComplaintServiceImpl complaintService;
    private Complaint correctComplaint;
    private ComplaintResponse correctComplaintResponse;
    private SuccessCreatedComplaint successCreatedComplaint;
    private ComplaintProfile correctComplaintProfile;
    private List<Complaint> complaintList ;
    private Club club;
    private User user;
    private ComplaintArch complaintArch;
    private List<ComplaintResponse> complaintResponseList;

    @BeforeEach
    public  void  setMocks(){

        user =  User.builder().id(USER_ID).build();
        club = Club.builder().id(CLUB_ID).build();
        correctComplaint = Complaint.builder()
                .id(CORRECT_COMPLAINT_ID)
                .club(club)
                .build();
        correctComplaintResponse = ComplaintResponse.builder()
                .id(CORRECT_COMPLAINT_ID)
                .build();
        successCreatedComplaint = SuccessCreatedComplaint.builder()
                .id(CORRECT_COMPLAINT_ID)
                .build();
        correctComplaintProfile = ComplaintProfile.builder()
                .id(CORRECT_COMPLAINT_ID)
                .clubId(CLUB_ID)
                .build();
        complaintArch = ComplaintArch.builder()
                .id(CORRECT_COMPLAINT_ID)
                .clubId(CLUB_ID)
                .build();

        complaintList = new ArrayList<>();
        complaintList.add(correctComplaint);
        complaintList.add(correctComplaint);

        complaintResponseList = new ArrayList<>();
        complaintResponseList.add(correctComplaintResponse);
        complaintResponseList.add(correctComplaintResponse);
    }

    @Test
    public  void getAllByClubIdMustReturnListOfComplaintResponse(){
        when(complaintRepository.getAllByClubId(CLUB_ID)).thenReturn(complaintList);
        when(dtoConverter.convertToDto(correctComplaint,ComplaintResponse.class)).thenReturn(correctComplaintResponse);
        List<ComplaintResponse> expectedList = complaintService.getAllByClubId(CLUB_ID);
        assertTrue(expectedList.contains(correctComplaintResponse));
    }

    @Test
    public  void getAllComplaintMustReturnListOfComplaintResponse(){
        when(complaintRepository.findAll()).thenReturn(complaintList);
        when((dtoConverter.convertToDto(correctComplaint,ComplaintResponse.class))).thenReturn(correctComplaintResponse);
        List<ComplaintResponse> expectedList = complaintService.getAll();
        assertThat(expectedList).isEqualTo(complaintResponseList);

    }

    @Test
    public void getComplaintByExistingIdMustReturnCorrectComplaint(){
        when(complaintRepository.findById(CORRECT_COMPLAINT_ID)).thenReturn(Optional.of(correctComplaint));
        Complaint expectedComplaint = complaintService.getComplaintById(CORRECT_COMPLAINT_ID);
        assertThat(expectedComplaint).isEqualTo(correctComplaint);
    }

    @Test
    public void getComplaintByWrongIdMustReturnNotExistException(){
        when(complaintRepository.findById(WRONG_COMPLAINT_ID)).thenReturn(Optional.empty());
        NotExistException exception  = assertThrows(NotExistException.class, () -> complaintService.getComplaintById(WRONG_COMPLAINT_ID));
        assertThat(exception.getMessage()).contains(WRONG_COMPLAINT_ID.toString());

    }

    @Test
     public void addComplaintMustReturnSuccessCreatedComplaint(){

        when(userService.getUserFromRequest(httpServletRequest)).thenReturn(user);
        when(dtoConverter.convertToEntity(any(ComplaintProfile.class),any(Complaint.class)))
                .thenReturn(new Complaint());
        when(clubRepository.existsById(CLUB_ID)).thenReturn(true);
        when(userRepository.existsById(USER_ID)).thenReturn(true);
        when(complaintRepository.save(correctComplaint)).thenReturn(correctComplaint);
        when(dtoConverter.convertToDto(correctComplaint,SuccessCreatedComplaint.class))
            .thenReturn(successCreatedComplaint);
        SuccessCreatedComplaint expected = complaintService.addComplaint(correctComplaintProfile,httpServletRequest);
        assertThat(expected).isEqualTo(successCreatedComplaint);


    }

    @Test
    public   void deleteComplaintByExistingIdMustReturnComplaintResponse(){
            when(complaintRepository.findById(CORRECT_COMPLAINT_ID)).thenReturn(Optional.of(correctComplaint));
            when(dtoConverter.convertToDto(correctComplaint, ComplaintResponse.class)).thenReturn(correctComplaintResponse);
            when(dtoConverter.convertToDto(correctComplaint, ComplaintArch.class)).thenReturn(complaintArch);
            when(archiveService.saveModel(complaintArch)).thenReturn(Archive.builder().build());
            assertThat(complaintService.deleteComplaintById(CORRECT_COMPLAINT_ID)).isEqualTo(correctComplaintResponse);
    }

    @Test
   public void deleteComplaintByIdUnSuccessDeletingMustReturnDatabaseRepositoryException(){
        when(complaintRepository.findById(CORRECT_COMPLAINT_ID)).thenReturn(Optional.of(correctComplaint));
        doThrow(DatabaseRepositoryException.class).when(complaintRepository).deleteById(CORRECT_COMPLAINT_ID);
        DatabaseRepositoryException expectedException =assertThrows(DatabaseRepositoryException.class,()-> complaintService.deleteComplaintById(CORRECT_COMPLAINT_ID));
        assertEquals(expectedException.getClass(),DatabaseRepositoryException.class);
    }

    @Test
   public void  getComplaintProfileByIdMustReturnComplaintResponse(){
            when(complaintRepository.findById(CORRECT_COMPLAINT_ID)).thenReturn(Optional.of(correctComplaint));
            when(dtoConverter.convertToDto(correctComplaint,ComplaintResponse.class))
                    .thenReturn(correctComplaintResponse);
            ComplaintResponse expected = complaintService.getComplaintProfileById(CORRECT_COMPLAINT_ID);
            System.out.println(expected);
            assertThat(expected).isEqualTo(correctComplaintResponse);
    }


   @Test
   public  void updateComplaintProfileByIdMustReturnComplaintProfile(){
       when(complaintRepository.findById(CORRECT_COMPLAINT_ID)).thenReturn(Optional.of(correctComplaint));
       when(dtoConverter.convertToEntity(correctComplaintProfile,correctComplaint)).thenReturn(correctComplaint);
       when(complaintRepository.save(correctComplaint)).thenReturn(correctComplaint);
       when(dtoConverter.convertToDto(correctComplaint,ComplaintProfile.class)).thenReturn(correctComplaintProfile);
       ComplaintProfile expectedComplaintProfile = complaintService.updateComplaintProfileById(CORRECT_COMPLAINT_ID,correctComplaintProfile);
       assertThat(expectedComplaintProfile).isEqualTo(correctComplaintProfile);
   }

}