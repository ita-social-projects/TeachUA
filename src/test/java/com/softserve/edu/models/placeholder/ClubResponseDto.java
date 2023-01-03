package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

public class ClubResponseDto extends BaseDto {

    private Integer id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String urlBackground;
    private ArrayList<GalleryPhotoDto> urlGallery;
    private String workTime;
    private ArrayList<CategoryDto> categories;
    private UserPreviewDto user;
    private CenterForClubDto center;
    private Double rating;
    private ArrayList<LocationResponseDto> locations;
    private Boolean isApproved;
    private Boolean isOnline;
    private Integer feedbackCount;
    private ArrayList<ContactDataResponseDto> contacts;

    /*
     * Method using for build new ClubResponseDto payload
     */
    public static Builder newBuilder() {
        return new ClubResponseDto().new Builder();
    }

    public Integer getId() {
        return this.id;
    }

    public ClubResponseDto setId(Integer id) {
        this.id = id;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Integer getAgeFrom() {
        return this.ageFrom;
    }

    public ClubResponseDto setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Integer getAgeTo() {
        return this.ageTo;
    }

    public ClubResponseDto setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ClubResponseDto setName(String name) {
        this.name = name;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public ClubResponseDto setDescription(String description) {
        this.description = description;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getUrlWeb() {
        return this.urlWeb;
    }

    public ClubResponseDto setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public ClubResponseDto setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getUrlBackground() {
        return this.urlBackground;
    }

    public ClubResponseDto setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<GalleryPhotoDto> getUrlGallery() {
        return this.urlGallery;
    }

    public ClubResponseDto setUrlGallery(ArrayList<GalleryPhotoDto> urlGallery) {
        this.urlGallery = urlGallery;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getWorkTime() {
        return this.workTime;
    }

    public ClubResponseDto setWorkTime(String workTime) {
        this.workTime = workTime;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<CategoryDto> getCategories() {
        return this.categories;
    }

    public ClubResponseDto setCategories(ArrayList<CategoryDto> categories) {
        this.categories = categories;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getUser() {
        return this.user;
    }

    public ClubResponseDto setUser(UserPreviewDto user) {
        this.user = user;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getCenter() {
        return this.center;
    }

    public ClubResponseDto setCenter(
            CenterForClubDto center) {
        this.center = center;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Double getRating() {
        return this.rating;
    }

    public ClubResponseDto setRating(Double rating) {
        this.rating = rating;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<LocationResponseDto> getLocations() {
        return this.locations;
    }

    public ClubResponseDto setLocations(ArrayList<LocationResponseDto> locations) {
        this.locations = locations;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsApproved() {
        return this.isApproved;
    }

    public ClubResponseDto setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsOnline() {
        return this.isOnline;
    }

    public ClubResponseDto setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Integer getFeedbackCount() {
        return this.feedbackCount;
    }

    public ClubResponseDto setFeedbackCount(Integer feedbackCount) {
        this.feedbackCount = feedbackCount;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<ContactDataResponseDto> getContacts() {
        return this.contacts;
    }

    public ClubResponseDto setContacts(ArrayList<ContactDataResponseDto> contacts) {
        this.contacts = contacts;
        // Methods are invoked on objects, this refers to the object on which the current method is called
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
        ClubResponseDto clubResponseDto = (ClubResponseDto) o;
        return new EqualsBuilder()
                .append(id, clubResponseDto.id)
                .append(ageFrom, clubResponseDto.ageFrom)
                .append(ageTo, clubResponseDto.ageTo)
                .append(name, clubResponseDto.name)
                .append(description, clubResponseDto.description)
                .append(urlWeb, clubResponseDto.urlWeb)
                .append(urlLogo, clubResponseDto.urlLogo)
                .append(urlBackground, clubResponseDto.urlBackground)
                .append(urlGallery, clubResponseDto.urlGallery)
                .append(workTime, clubResponseDto.workTime)
                .append(categories, clubResponseDto.categories)
                .append(user, clubResponseDto.user)
                .append(center, clubResponseDto.center)
                .append(rating, clubResponseDto.rating)
                .append(locations, clubResponseDto.locations)
                .append(isApproved, clubResponseDto.isApproved)
                .append(isOnline, clubResponseDto.isOnline)
                .append(feedbackCount, clubResponseDto.feedbackCount)
                .append(contacts, clubResponseDto.contacts)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(ageFrom)
                .append(ageTo)
                .append(name)
                .append(description)
                .append(urlWeb)
                .append(urlLogo)
                .append(urlBackground)
                .append(urlGallery)
                .append(workTime)
                .append(categories)
                .append(user)
                .append(center)
                .append(rating)
                .append(locations)
                .append(isApproved)
                .append(isOnline)
                .append(feedbackCount)
                .append(contacts)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ClubDto{" +
                "id=" + id +
                ", ageFrom=" + ageFrom +
                ", ageTo=" + ageTo +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", urlWeb=" + urlWeb +
                ", urlLogo='" + urlLogo + '\'' +
                ", urlBackground='" + urlBackground + '\'' +
                ", urlGallery=" + urlGallery +
                ", workTime=" + workTime +
                ", categories=" + categories +
                ", user=" + user +
                ", center=" + center +
                ", rating=" + rating +
                ", locations=" + locations +
                ", isApproved=" + isApproved +
                ", isOnline=" + isOnline +
                ", feedbackCount=" + feedbackCount +
                ", contacts=" + contacts +
                '}';
    }

    public class Builder {

        // Constructor (here we need to add only optional fields to initialize them because mandatory field will be
        // initialized anyway)
        private Builder() {
        }

        public Builder withId(Integer id) {
            ClubResponseDto.this.id = id;
            return this;
        }

        public Builder withAgeFrom(Integer ageFrom) {
            ClubResponseDto.this.ageFrom = ageFrom;
            return this;
        }

            public Builder withAgeTo(Integer ageTo) {
            ClubResponseDto.this.ageTo = ageTo;
            return this;
        }

        public Builder withName(String name) {
            ClubResponseDto.this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            ClubResponseDto.this.description = description;
            return this;
        }

        public Builder withUrlWeb(String urlWeb) {
            ClubResponseDto.this.urlWeb = urlWeb;
            return this;
        }

        public Builder withUrlLogo(String urlLogo) {
            ClubResponseDto.this.urlLogo = urlLogo;
            return this;
        }

        public Builder withUrlBackground(String urlBackground) {
            ClubResponseDto.this.urlBackground = urlBackground;
            return this;
        }

        public Builder withUrlGallery(ArrayList<GalleryPhotoDto> urlGallery) {
            ClubResponseDto.this.urlGallery = urlGallery;
            return this;
        }

        public Builder withWorkTime(String workTime) {
            ClubResponseDto.this.workTime = workTime;
            return this;
        }

        public Builder withCategories(ArrayList<CategoryDto> categories) {
            ClubResponseDto.this.categories = categories;
            return this;
        }

        public Builder withUser(UserPreviewDto user) {
            ClubResponseDto.this.user = user;
            return this;
        }

        public Builder withCenter(CenterForClubDto center) {
            ClubResponseDto.this.center = center;
            return this;
        }

        public Builder withRating(Double rating) {
            ClubResponseDto.this.rating = rating;
            return this;
        }

        public Builder withLocations(ArrayList<LocationResponseDto> locations) {
            ClubResponseDto.this.locations = locations;
            return this;
        }

        public Builder withIsApproved(Boolean isApproved) {
            ClubResponseDto.this.isApproved = isApproved;
            return this;
        }

        public Builder withIsOnline(Boolean isOnline) {
            ClubResponseDto.this.isOnline = isOnline;
            return this;
        }

        public Builder withFeedbackCount(Integer feedbackCount) {
            ClubResponseDto.this.feedbackCount = feedbackCount;
            return this;
        }

        public Builder withContacts(ArrayList<ContactDataResponseDto> contacts) {
            ClubResponseDto.this.contacts = contacts;
            return this;
        }

        public ClubResponseDto build() {
            return ClubResponseDto.this;
        }

    }

}
