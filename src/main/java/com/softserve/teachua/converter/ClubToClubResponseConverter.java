package com.softserve.teachua.converter;

import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.service.ContactTypeService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
@Slf4j

@Component
public class ClubToClubResponseConverter {

    private ContactsStringConverter contactsStringConverter;
    private DtoConverter dtoConverter;

    @Autowired
    public ClubToClubResponseConverter(ContactsStringConverter contactsStringConverter,
                                       DtoConverter dtoConverter){
        this.contactsStringConverter=contactsStringConverter;
        this.dtoConverter=dtoConverter;
    }

    /**
     * The method returns dto {@code ClubResponse} of club and fetch data by contact_type_id
     * from contact_types table from DB to fulfill contact field with consistent data.
     *
     * @param club - put club .
     * @return new {@code ClubResponse}.
     */
    public ClubResponse convertToClubResponse(Club club){

        ClubResponse clubResponse=dtoConverter.convertToDto(club,ClubResponse.class);
        clubResponse.setContacts(
                contactsStringConverter.convertStringToContactDataResponses(club.getContacts()));
        return clubResponse;
    }
}
