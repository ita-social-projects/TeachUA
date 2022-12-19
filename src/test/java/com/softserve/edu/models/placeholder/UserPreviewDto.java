package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class UserPreviewDto extends BaseDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String urlLogo;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPreviewDto userPreviewDto = (UserPreviewDto) o;

        return new EqualsBuilder()
                .append(id, userPreviewDto.id)
                .append(firstName, userPreviewDto.firstName)
                .append(lastName, userPreviewDto.lastName)
                .append(urlLogo, userPreviewDto.urlLogo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(firstName)
                .append(lastName)
                .append(urlLogo)
                .toHashCode();
    }

}
