package com.softserve.teachua.converter;

import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.service.ContactTypeService;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Vasyl Khula
 * */

@NoArgsConstructor
@Data
@EqualsAndHashCode
@Slf4j

@Component
public class ContactsStringConverter {

    private ContactTypeService contactTypeService;

    @Autowired
    public ContactsStringConverter(ContactTypeService contactTypeService){
        this.contactTypeService=contactTypeService;
    }

    /**
     * The method returns {@code Set<ContactDataResponse>} after parsing string contacts from center object
     *
     * @param contacts - put center .
     * @return new {@code Set<ContactDataResponse>}.
     */
    public Set<ContactDataResponse> convertStringToContactDataResponses(String contacts){
        if(contacts==null || contacts.isEmpty()){
            log.info("contacts string is null or empty");
            return new HashSet<>();
        }

        String[] singleContact=contacts.split(",");
        Set<ContactDataResponse> result=new HashSet<>();
        try{
            for (String s: singleContact) {
                s = s.replaceAll("[\\{\\}\" ]","");
                String[] data = s.split("::");
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
