package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ContactTypeDto extends BaseDto {

    private int id;
    private String name;
    private String urlLogo;

    public int getId() {
        return this.id;
    }

    public ContactTypeDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ContactTypeDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public ContactTypeDto setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
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
        ContactTypeDto contactTypeDto = (ContactTypeDto) o;
        return new EqualsBuilder()
                .append(id, contactTypeDto.id)
                .append(name, contactTypeDto.name)
                .append(urlLogo, contactTypeDto.urlLogo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(urlLogo)
                .toHashCode();
    }
}
