package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ContactDto extends BaseDto {

    private ContactTypeDto contactType;
    private String contactData;

    public ContactTypeDto getContactType() {
        return this.contactType;
    }

    public ContactDto setContactType(ContactTypeDto contactType) {
        this.contactType = contactType;
        return this;
    }

    public String getContactData() {
        return this.contactData;
    }

    public ContactDto setContactData(String contactData) {
        this.contactData = contactData;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContactDto contactDto = (ContactDto) o;

        return new EqualsBuilder()
                .append(contactType, contactDto.contactType)
                .append(contactData, contactDto.contactData)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(contactType)
                .append(contactData)
                .toHashCode();
    }

}
