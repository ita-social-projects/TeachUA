package com.softserve.teachua.converter;

import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.model.ContactType;
import com.softserve.teachua.service.ContactTypeService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Slf4j
@Component
public class ContactsStringConverter {
    private ContactTypeService contactTypeService;

    @Autowired
    public ContactsStringConverter(ContactTypeService contactTypeService) {
        this.contactTypeService = contactTypeService;
    }

    /**
     * The method returns {@code Set<ContactDataResponse>} after parsing string contacts from center object.
     *
     * @param contacts - put center .
     * @return new {@code Set<ContactDataResponse>}.
     */
    public Set<ContactDataResponse> convertStringToContactDataResponses(String contacts) {
        if (contacts == null || contacts.isEmpty()) {
            log.debug("contacts string is null or empty");
            return new HashSet<>();
        }

        String[] singleContact = contacts.split(",");
        Set<ContactDataResponse> result = new HashSet<>();
        try {
            for (String s : singleContact) {
                s = s.replaceAll("[\\{\\}\" ]", "");
                String[] data = s.split("::");
                if (data.length < 2) {
                    continue;
                }
                ContactType contactType = contactTypeService.getContactTypeById(Long.parseLong(data[0]));
                result.add(new ContactDataResponse(contactType, data[1]));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return result;
        }

        return result;
    }

    public String convertContactDataResponseToString(Set<ContactDataResponse> contactDataResponses){
        StringBuilder result= new StringBuilder();
            try {
                for (ContactDataResponse contactResponse: contactDataResponses) {
                    result.append(contactResponse.getContactType().getId());
                    result.append("::");
                    result.append(contactResponse.getContactData());
                    result.append(", ");
                }
                result.deleteCharAt(result.lastIndexOf(","));
            }catch (Exception e){
                log.error(e.getMessage());
            }

        return result.toString();
    }
}
