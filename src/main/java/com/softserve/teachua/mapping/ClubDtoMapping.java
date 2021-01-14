package com.softserve.teachua.mapping;

import com.softserve.teachua.dto.ClubDto;
import com.softserve.teachua.entity.Club;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class ClubDtoMapping extends AbstractConverter<Club, ClubDto> {
    @Override
    protected ClubDto convert(Club club) {
        return ClubDto.builder()
            .id(club.getId())
            .clubName(club.getClubName())
            .address(club.getChildrenCenter().getAddress())
            .generalDescriptionOfClub(club.getGeneralDescriptionOfClub())
            .logoUrl(club.getLogoUrl())
            .childrenCenterId(club.getChildrenCenter().getId())
            .build();
    }
}
