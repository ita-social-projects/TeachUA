package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CategoryDto extends BaseDto {

    private int id;
    private int sortby;
    private String name;
    private String description;
    private String urlLogo;
    private String backgroundColor;
    private String tagBackgroundColor;
    private String tagTextColor;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSortBy() {
        return this.sortby;
    }

    public void setSortBy(int sortby) {
        this.sortby = sortby;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTagBackgroundColor() {
        return this.tagBackgroundColor;
    }

    public void setTagBackgroundColor(String tagBackgroundColor) {
        this.tagBackgroundColor = tagBackgroundColor;
    }

    public String getTagTextColor() {
        return this.tagTextColor;
    }

    public void setTagTextColor(String tagTextColor) {
        this.tagTextColor = tagTextColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryDto categoryDto = (CategoryDto) o;

        return new EqualsBuilder()
                .append(id, categoryDto.id)
                .append(sortby, categoryDto.sortby)
                .append(name, categoryDto.name)
                .append(description, categoryDto.description)
                .append(urlLogo, categoryDto.urlLogo)
                .append(backgroundColor, categoryDto.backgroundColor)
                .append(tagBackgroundColor, categoryDto.tagBackgroundColor)
                .append(tagTextColor, categoryDto.tagTextColor)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(sortby)
                .append(name)
                .append(description)
                .append(urlLogo)
                .append(backgroundColor)
                .append(tagBackgroundColor)
                .append(tagTextColor)
                .toHashCode();
    }
}
