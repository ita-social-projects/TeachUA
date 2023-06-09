package com.softserve.club.dto.contact_data;

import com.softserve.club.model.ContactType;
import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@Component
public class ContactDataResponse implements Convertible {
    private ContactType contactType;
    private String contactData;
}
