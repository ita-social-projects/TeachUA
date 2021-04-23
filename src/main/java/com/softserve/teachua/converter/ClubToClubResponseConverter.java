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

    private ContactTypeService contactTypeService;

    private DtoConverter dtoConverter;

    @Autowired
    public ClubToClubResponseConverter(ContactTypeService contactTypeService,DtoConverter dtoConverter){
        this.contactTypeService=contactTypeService;
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
                convertStringToContactDataResponses(club.getContacts()));
        log.info("===  convertToClubResponse method"+clubResponse);
        return clubResponse;
    }

    /**
     * The method returns {@code Set<ContactDataResponse>} after parsing string contacts from club object
     *
     * @param contacts - put club .
     * @return new {@code Set<ContactDataResponse>}.
     */
    private Set<ContactDataResponse> convertStringToContactDataResponses(String contacts){
        if(contacts==null || contacts.isEmpty()){
            log.info("contacts string is null or empty");
            return new HashSet<>();
        }

        String[] singleContact=contacts.split(",");
        Set<ContactDataResponse> result=new HashSet<>();
        try{
            for (String s: singleContact) {
                s = s.replaceAll("[\\{\\}\" ]","");
                String[] data = s.split(":");
                ContactType contactType=contactTypeService.getContactTypeById(Long.parseLong(data[0]));
                result.add(new ContactDataResponse(contactType,data[1]));
            }
        }catch (NumberFormatException e){
            log.info(e.getMessage());
            return result;
        }

        log.info("contacts field =="+result.toString());
        return result;
    }
}
