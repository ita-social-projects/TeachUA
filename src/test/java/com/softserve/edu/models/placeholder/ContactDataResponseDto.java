package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ContactDataResponseDto extends BaseDto {

    private ContactTypeDto contactType;
    private String contactData;

    public ContactTypeDto getContactType() {
        return this.contactType;
    }

    public ContactDataResponseDto setContactType(ContactTypeDto contactType) {
        this.contactType = contactType;
        return this;
    }

    public String getContactData() {
        return this.contactData;
    }

    public ContactDataResponseDto setContactData(String contactData) {
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
        ContactDataResponseDto contactDataResponseDto = (ContactDataResponseDto) o;

        return new EqualsBuilder()
                .append(contactType, contactDataResponseDto.contactType)
                .append(contactData, contactDataResponseDto.contactData)
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
