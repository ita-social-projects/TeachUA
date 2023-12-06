package com.softserve.teachua.dto.contact_data;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.ContactType;
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
