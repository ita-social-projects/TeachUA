package com.softserve.teachua.service;


import com.softserve.teachua.dto.center.CenterProfile;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.dto.center.SuccessCreatedCenter;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.User;
import com.softserve.teachua.repository.CenterRepository;
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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CenterServiceTest {
    @MockBean
    private CenterRepository centerRepository;

    private final CenterService centerService;

    private Center center1;
    private Center center2;

    private CenterProfile center1Profile;
    private CenterProfile center2Profile;

    private List<Center> centerList;

    private static final String CENTER_1_NAME = "CENTER_1_NAME";
    private static final String CENTER_2_NAME = "CENTER_2_NAME";
    private static final String SOME_NOT_EXISTS_NAME = "SOME_NOT_EXISTS_NAME";

    private static final long CENTER_1_ID = 1L;
    private static final long CENTER_2_ID = 200L;
    private static final long SOME_NOT_EXISTS_CENTER_ID = 500L;

    @Autowired
    public CenterServiceTest(CenterService centerService) {
        this.centerService = centerService;
    }

    @BeforeEach
    void intiializeAndMockMethods() {
        center1 = new Center();
        center2 = new Center();

        center1Profile = new CenterProfile();
        center2Profile = new CenterProfile();

        centerList = new ArrayList<>();
        centerList.add(center1);
        centerList.add(center2);

        center1.setName(CENTER_1_NAME);
        center1.setId(CENTER_1_ID);

        center2.setName(CENTER_2_NAME);
        center2.setId(CENTER_2_ID);

        center1Profile.setName(CENTER_1_NAME);
        center1Profile.setId(CENTER_1_ID);

        center2Profile.setName(CENTER_2_NAME);
        center2Profile.setId(CENTER_2_ID);

        Mockito.when(centerRepository.findById(CENTER_1_ID)).thenReturn(Optional.of(center1));
        Mockito.when(centerRepository.findById(CENTER_2_ID)).thenReturn(Optional.of(center2));
        Mockito.when(centerRepository.findById(Mockito.argThat(arg -> arg != CENTER_1_ID && arg != CENTER_2_ID))).thenReturn(Optional.empty());

        Mockito.when(centerRepository.findByName(CENTER_1_NAME)).thenReturn(Optional.of(center1));
        Mockito.when(centerRepository.findByName(CENTER_2_NAME)).thenReturn(Optional.of(center2));
        Mockito.when(centerRepository.findByName(Mockito.argThat(arg -> !arg.equals(CENTER_1_NAME) && !arg.equals(CENTER_2_NAME)))).thenReturn(Optional.empty());

        Mockito.when(centerRepository.existsByName(CENTER_1_NAME)).thenReturn(true);
        Mockito.when(centerRepository.existsByName(CENTER_2_NAME)).thenReturn(true);
        Mockito.when(centerRepository.existsByName(Mockito.argThat(arg -> !arg.equals(CENTER_1_NAME) && !arg.equals(CENTER_2_NAME)))).thenReturn(false);

        Mockito.when(centerRepository.findAll()).thenReturn(centerList);

        Mockito.when(centerRepository.save(Mockito.any(Center.class)))
                .thenAnswer(invocation -> invocation.getArguments()[0]);

    }

    @Test
    void testGetCenterById() {
        Center actualCenter1 = centerService.getCenterById(CENTER_1_ID);
        Center actualCenter2 = centerService.getCenterById(CENTER_2_ID);

        assertThat(actualCenter1.getId()).isEqualTo(CENTER_1_ID);
        assertThat(actualCenter2.getId()).isEqualTo(CENTER_2_ID);

        assertThat(actualCenter1.getName()).isEqualTo(CENTER_1_NAME);
        assertThat(actualCenter2.getName()).isEqualTo(CENTER_2_NAME);

        assertThat(actualCenter1).isEqualTo(center1);
        assertThat(actualCenter2).isEqualTo(center2);

        assertThatThrownBy(() -> {
            centerService.getCenterById(SOME_NOT_EXISTS_CENTER_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void testGetCenterByProfileId() {
        CenterResponse center1Response = centerService.getCenterByProfileId(CENTER_1_ID);
        CenterResponse center2Response = centerService.getCenterByProfileId(CENTER_2_ID);

        assertThat(center1Response.getId()).isEqualTo(CENTER_1_ID);
        assertThat(center2Response.getId()).isEqualTo(CENTER_2_ID);

        assertThat(center1Response.getName()).isEqualTo(CENTER_1_NAME);
        assertThat(center2Response.getName()).isEqualTo(CENTER_2_NAME);

        assertThatThrownBy(() -> {
            centerService.getCenterByProfileId(SOME_NOT_EXISTS_CENTER_ID);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void testGetCenterByName() {
        Center actualCenter1 = centerService.getCenterByName(CENTER_1_NAME);
        Center actualCenter2 = centerService.getCenterByName(CENTER_2_NAME);

        assertThat(actualCenter1.getId()).isEqualTo(CENTER_1_ID);
        assertThat(actualCenter2.getId()).isEqualTo(CENTER_2_ID);

        assertThat(actualCenter1.getName()).isEqualTo(CENTER_1_NAME);
        assertThat(actualCenter2.getName()).isEqualTo(CENTER_2_NAME);

        assertThatThrownBy(() -> {
            centerService.getCenterByName(SOME_NOT_EXISTS_NAME);
        }).isInstanceOf(NotExistException.class);
    }

    @Test
    void testAddCenter() {
        CenterProfile newCenter = new CenterProfile();
        newCenter.setName(SOME_NOT_EXISTS_NAME);
        newCenter.setId(SOME_NOT_EXISTS_CENTER_ID);
        newCenter.setAddress("_");
        newCenter.setDescription("_");
        newCenter.setEmail("_");
        newCenter.setLatitude(0.0);
        newCenter.setLongitude(0.0);
        newCenter.setPhones("_");
        newCenter.setSocialLinks("_");
        newCenter.setUrlLogo("_");
        newCenter.setUrlWeb("_");
        newCenter.setUser(new User());
        SuccessCreatedCenter createdCenter = centerService.addCenter(newCenter);


        assertThat(createdCenter.getId()).isEqualTo(SOME_NOT_EXISTS_CENTER_ID);

        assertThat(createdCenter.getName()).isEqualTo(SOME_NOT_EXISTS_NAME);

        assertThatThrownBy(() -> {
            centerService.addCenter(center1Profile);
        }).isInstanceOf(AlreadyExistException.class);

        assertThatThrownBy(() -> {
            centerService.addCenter(center2Profile);
        }).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void testGetListOfCenters() {
        List<CenterResponse> centerResponseList = centerService.getListOfCenters();

        Set<Center> actualCenters = centerResponseList.stream().map(r -> {
            Center center = new Center();
            center.setName(r.getName());
            center.setId(r.getId());

            return center;
        }).collect(Collectors.toSet());

        Set<Center> expectedCenters = new HashSet<>(centerList);

        assertThat(actualCenters.size()).isEqualTo(expectedCenters.size());

        assertThat(actualCenters).isEqualTo(expectedCenters);
    }

    @Test
    void testUpdateCenter() {
        final String NEW_NAME_1 = "New name 1";
        final String NEW_NAME_2 = "New name 2";

        CenterProfile newCenterProfile1 = new CenterProfile();
        CenterProfile newCenterProfile2 = new CenterProfile();

        newCenterProfile1.setName(NEW_NAME_1);
        newCenterProfile2.setName(NEW_NAME_2);

        newCenterProfile1.setId(CENTER_1_ID);
        newCenterProfile2.setId(CENTER_2_ID);

        CenterProfile updatedProfile1 = centerService.updateCenter(CENTER_1_ID, newCenterProfile1);
        CenterProfile updatedProfile2 = centerService.updateCenter(CENTER_2_ID, newCenterProfile2);

        assertThat(updatedProfile1.getName()).isEqualTo(NEW_NAME_1);
        assertThat(updatedProfile2.getName()).isEqualTo(NEW_NAME_2);

        assertThat(updatedProfile1.getId()).isEqualTo(CENTER_1_ID);
        assertThat(updatedProfile2.getId()).isEqualTo(CENTER_2_ID);

        assertThat(updatedProfile1).isEqualTo(newCenterProfile1);
        assertThat(updatedProfile2).isEqualTo(newCenterProfile2);

        assertThatThrownBy(() -> {
            centerService.updateCenter(SOME_NOT_EXISTS_CENTER_ID, newCenterProfile1);
        }).isInstanceOf(NotExistException.class);
    }
    /*
    To Test:
        + CenterResponse getCenterByProfileId(Long id);
        + Center getCenterById(Long id);
        + Center getCenterByName(String name);
        - SuccessCreatedCenter addCenter(CenterProfile centerProfile);
        + List<CenterResponse> getListOfCenters();
        + CenterProfile updateCenter(Long id, CenterProfile centerProfile);
     */
}
