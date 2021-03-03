/*
package com.softserve.teachua.service;

import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.club.ClubProfile;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.SuccessCreatedClub;
import com.softserve.teachua.dto.club.SuccessUpdatedClub;
import com.softserve.teachua.dto.search.SearchClubProfile;
import com.softserve.teachua.dto.search.SearchPossibleResponse;
import com.softserve.teachua.dto.search.SimilarClubProfile;
import com.softserve.teachua.exception.AlreadyExistException;
import com.softserve.teachua.exception.NotExistException;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.repository.ClubRepository;
import com.softserve.teachua.service.impl.ClubServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private DtoConverter dtoConverter;

    @Mock
    private ArchiveService archiveService;

    @InjectMocks
    private ClubServiceImpl clubService;

    private Club club;
    private Club club4;
    private Club club5;
    private Club club6;

    private ClubProfile clubProfile;

    private final long EXISTING_ID = 3L;
    private final long NOT_EXISTING_ID = 500L;

    private final String NOT_EXISTING_NAME = "Club1";
    private final String EXISTING_NAME = "Club2";
    private final String NEW_NAME = "Club3";

    @BeforeEach
    public void setUp() {
        club = Club.builder()
                .id(EXISTING_ID)
                .name(EXISTING_NAME)
                .build();
        clubProfile = ClubProfile.builder()
                .name(NEW_NAME)
                .build();

        Set<Category> set1 = Collections.singleton(Category.builder().name("Спортивні секції").build());
        Set<Category> set2 = Collections.singleton(Category.builder().name("Спортивні секції").build());
        Set<Category> set3 = Collections.singleton(Category.builder().name("Танці").build());

        club4 = Club.builder().name("Клуб").city(City.builder().name("Lviv").build()).categories(set1).build();
        club5 = Club.builder().name("Клук").city(City.builder().name("Lviv").build()).categories(set2).build();
        club6 = Club.builder().name("Клік").city(City.builder().name("Lviv").build()).categories(set3).build();

    }

    @Test
    void getClubProfileById() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(dtoConverter.convertToDto(club, ClubResponse.class)).thenReturn(ClubResponse.builder().name(EXISTING_NAME).build());

        ClubResponse actual = clubService.getClubProfileById(EXISTING_ID);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void getClubById() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));

        Club actual = clubService.getClubById(EXISTING_ID);
        assertEquals(club, actual);
    }

    @Test
    void getClubByNotExistingId() {
        when(clubRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClubById(NOT_EXISTING_ID)).isInstanceOf(NotExistException.class);
    }

    @Test
    void getClubByName() {
        when(clubRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(club));

        Club actual = clubService.getClubByName(EXISTING_NAME);
        assertEquals(club, actual);
    }

    @Test
    void getClubByNotExistingName() {
        when(clubRepository.findByName(NOT_EXISTING_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.getClubByName(NOT_EXISTING_NAME)).isInstanceOf(NotExistException.class);
    }

    @Test
    void updateClub() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(clubRepository.save(any())).thenReturn(club);
        when(dtoConverter.convertToEntity(clubProfile, club)).thenReturn(Club.builder().name(clubProfile.getName()).build());
        when(dtoConverter.convertToDto(club, SuccessUpdatedClub.class)).thenReturn(SuccessUpdatedClub.builder().name(clubProfile.getName()).build());

        SuccessUpdatedClub actual = clubService.updateClub(EXISTING_ID, clubProfile);
        assertEquals(clubProfile.getName(), actual.getName());
    }

    @Test
    void getClubProfileByName() {
        when(clubRepository.findByName(EXISTING_NAME)).thenReturn(Optional.of(club));
        when(dtoConverter.convertToDto(club, ClubResponse.class)).thenReturn(ClubResponse.builder().name(EXISTING_NAME).build());

        ClubResponse actual = clubService.getClubProfileByName(EXISTING_NAME);
        assertEquals(club.getName(), actual.getName());
    }

    @Test
    void addClub() {
        Club newClub = Club.builder()
                .name(NEW_NAME)
                .build();
        when(clubRepository.existsByName(NEW_NAME)).thenReturn(false);
        when(dtoConverter.convertToEntity(clubProfile, new Club())).thenReturn(newClub);
        when(clubRepository.save(any())).thenReturn(newClub);
        when(dtoConverter.convertToDto(newClub, SuccessCreatedClub.class)).thenReturn(SuccessCreatedClub.builder().name(NEW_NAME).build());

        SuccessCreatedClub actual = clubService.addClub(clubProfile);
        assertEquals(clubProfile.getName(), actual.getName());
    }

    @Test
    void addExistingClub() {
        clubProfile.setName(EXISTING_NAME);
        when(clubRepository.existsByName(EXISTING_NAME)).thenReturn(true);

        assertThatThrownBy(() -> clubService.addClub(clubProfile)).isInstanceOf(AlreadyExistException.class);
    }

    @Test
    void getListOfClubs() {
        when(clubRepository.findAll()).thenReturn(Collections.singletonList(club));
        when(dtoConverter.convertToDto(club, ClubResponse.class)).thenReturn(ClubResponse.builder().name(EXISTING_NAME).build());

        List<ClubResponse> actual = clubService.getListOfClubs();
        assertEquals(1, actual.size());
    }

    @Test
    void getSimilarClubsByCategoryName() {
        Page<Club> clubs = new PageImpl<>(Arrays.asList(club4, club5));

        when(clubRepository.findTop2ByCategoryName(1L, "Спортивні секції", "Lviv", PageRequest.of(0, 2)))
                .thenReturn(clubs);
        for (Club c : clubs
        ) {
            when(dtoConverter.convertToDto(c, ClubResponse.class)).thenReturn(ClubResponse.builder().name(c.getName()).build());
        }

        List<ClubResponse> actual = clubService.getSimilarClubsByCategoryName(new SimilarClubProfile(1L, "Спортивні секції", "Lviv"));
        assertEquals(clubs.getSize(), actual.size());
    }

    @Test
    void getClubsBySearchParameters() {
        SearchClubProfile searchClubProfile = new SearchClubProfile("", "Lviv", "", "", "Спортивні секції");
        Page<Club> clubs = new PageImpl<>(Arrays.asList(club4, club5));
        Pageable pageable = PageRequest.of(0, 2);
        when(clubRepository.findAllByParameters(searchClubProfile.getClubName(),
                searchClubProfile.getCityName(),
                searchClubProfile.getDistrictName(),
                searchClubProfile.getStationName(),
                searchClubProfile.getCategoryName(),
                pageable)).thenReturn(clubs);
        for (Club c : clubs
        ) {
            when(dtoConverter.convertToDto(c, ClubResponse.class)).thenReturn(ClubResponse.builder().name(c.getName()).build());
        }

        Page<ClubResponse> actual = clubService.getClubsBySearchParameters(searchClubProfile, pageable);

        assertEquals(clubs.getSize(), actual.getSize());
        assertEquals(clubs.getContent().get(0).getName(), actual.getContent().get(0).getName());
        assertEquals(clubs.getContent().get(1).getName(), actual.getContent().get(1).getName());
    }

    @Test
    void getPossibleClubByName() {
        Page<Club> clubs = new PageImpl<>(Arrays.asList(club4, club5, club6));

        when(clubRepository.findTop3ByName("Кл", "Lviv", PageRequest.of(0, 3))).thenReturn(clubs);
        when(dtoConverter.convertToDto(club4, SearchPossibleResponse.class)).thenReturn(SearchPossibleResponse.builder().name(club4.getName()).build());
        when(dtoConverter.convertToDto(club5, SearchPossibleResponse.class)).thenReturn(SearchPossibleResponse.builder().name(club5.getName()).build());
        when(dtoConverter.convertToDto(club6, SearchPossibleResponse.class)).thenReturn(SearchPossibleResponse.builder().name(club6.getName()).build());

        List<SearchPossibleResponse> actual = clubService.getPossibleClubByName("Кл", "Lviv");
        assertEquals(club4.getName(), actual.get(0).getName());
    }

    @Test
    void deleteClubById() {
        when(clubRepository.findById(EXISTING_ID)).thenReturn(Optional.of(club));
        when(archiveService.saveModel(club)).thenReturn(club);
        doNothing().when(clubRepository).deleteById(EXISTING_ID);
        doNothing().when(clubRepository).flush();
        when(dtoConverter.convertToDto(club, ClubResponse.class)).thenReturn(ClubResponse.builder().name(club.getName()).build());

        ClubResponse actual = clubService.deleteClubById(EXISTING_ID);
        assertEquals(club.getName(), actual.getName());
    }
}*/
