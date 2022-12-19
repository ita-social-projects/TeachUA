package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class CategoryDto extends BaseDto {

    private Integer id;
    private Integer sortby;
    private String name;
    private String description;
    private String urlLogo;
    private String backgroundColor;
    private String tagBackgroundColor;
    private String tagTextColor;

    /*
     * Method using for build new CategoryDto payload
     */
    public static CategoryDto.Builder newBuilder() {
        return new CategoryDto().new Builder();
    }

    public Integer getId() {
        return this.id;
    }

    public CategoryDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getSortby() {
        return this.sortby;
    }

    public CategoryDto setSortby(Integer sortby) {
        this.sortby = sortby;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public CategoryDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public CategoryDto setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        return this;
    }

    public String getBackgroundColor() {
        return this.backgroundColor;
    }

    public CategoryDto setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public String getTagBackgroundColor() {
        return this.tagBackgroundColor;
    }

    public CategoryDto setTagBackgroundColor(String tagBackgroundColor) {
        this.tagBackgroundColor = tagBackgroundColor;
        return this;
    }

    public String getTagTextColor() {
        return this.tagTextColor;
    }

    public CategoryDto setTagTextColor(String tagTextColor) {
        this.tagTextColor = tagTextColor;
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

    public class Builder {
        private Builder() {
        }

        public Builder withId(Integer id) {
            CategoryDto.this.id = id;
            return this;
        }

        public Builder withSortBy(Integer sortBy) {
            CategoryDto.this.sortby = sortBy;
            return this;
        }

        public Builder withName(String name) {
            CategoryDto.this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            CategoryDto.this.description = description;
            return this;
        }

        public Builder withUrlLogo(String urlLogo) {
            CategoryDto.this.urlLogo = urlLogo;
            return this;
        }

        public Builder withBackgroundColor(String backgroundColor) {
            CategoryDto.this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder withTagBackgroundColor(String tagBackgroundColor) {
            CategoryDto.this.tagBackgroundColor = tagBackgroundColor;
            return this;
        }

        public Builder withTagTextColor(String tagTextColor) {
            CategoryDto.this.tagTextColor = tagTextColor;
            return this;
        }

        public CategoryDto build() {
            return CategoryDto.this;
        }

    }

}
