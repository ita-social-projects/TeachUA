package com.softserve.teachua.converter;

import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.club.MessagesClub;
import com.softserve.teachua.model.Club;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

class DtoConverterTest {
    @InjectMocks
    private DtoConverter dtoConverter;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldConvertToEntity() {
        ClubResponse clubDto = new ClubResponse();
        Club club = new Club();

        dtoConverter.convertToEntity(clubDto, club);

        verify(modelMapper, times(1)).map(clubDto, (Type) club.getClass());
    }

    @Test
    void shouldConvertToDto() {
        ClubResponse clubDto = new ClubResponse();
        Club club = new Club();

        dtoConverter.convertToDto(club, clubDto.getClass());

        verify(modelMapper, times(1)).map(club, (Type) clubDto.getClass());
    }

    @Test
    void shouldConvertEntityToDto() {
        ClubResponse clubDto = new ClubResponse();
        Club club = new Club();

        dtoConverter.convertToDto(club, clubDto);

        verify(modelMapper, times(1)).map(club, (Type) clubDto.getClass());
    }

    @Test
    void shouldConvertFromDtoToDto() {
        ClubResponse clubDto = new ClubResponse();
        MessagesClub anotherClubDto = new MessagesClub();

        dtoConverter.convertFromDtoToDto(clubDto, anotherClubDto);

        verify(modelMapper, times(1)).map(clubDto, (Type) anotherClubDto.getClass());
    }
}
