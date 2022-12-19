package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class ClubDto extends BaseDto {

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
    private UserPreviewDto[] user;
    private CenterForClubDto[] center;
    private Double rating;
    private ArrayList<LocationResponseDto> locations;
    private Boolean isApproved;
    private Boolean isOnline;
    private Integer feedbackCount;
    private ArrayList<ContactDataResponseDto> contacts;

    /*
     * Method using for build new ClubDto payload
     */
    public static Builder newBuilder() {
        return new ClubDto().new Builder();
    }

    public Integer getId() {
        return this.id;
    }

    public ClubDto setId(Integer id) {
        this.id = id;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Integer getAgeFrom() {
        return this.ageFrom;
    }

    public ClubDto setAgeFrom(Integer ageFrom) {
        this.ageFrom = ageFrom;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Integer getAgeTo() {
        return this.ageTo;
    }

    public ClubDto setAgeTo(Integer ageTo) {
        this.ageTo = ageTo;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ClubDto setName(String name) {
        this.name = name;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public ClubDto setDescription(String description) {
        this.description = description;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getUrlWeb() {
        return this.urlWeb;
    }

    public ClubDto setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public ClubDto setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getUrlBackground() {
        return this.urlBackground;
    }

    public ClubDto setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<GalleryPhotoDto> getUrlGallery() {
        return this.urlGallery;
    }

    public ClubDto setUrlGallery(ArrayList<GalleryPhotoDto> urlGallery) {
        this.urlGallery = urlGallery;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getWorkTime() {
        return this.workTime;
    }

    public ClubDto setWorkTime(String workTime) {
        this.workTime = workTime;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<CategoryDto> getCategories() {
        return this.categories;
    }

    public ClubDto setCategories(ArrayList<CategoryDto> categories) {
        this.categories = categories;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getUser() {
        return this.user;
    }

    public ClubDto setUser(UserPreviewDto[] user) {
        this.user = user;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getCenter() {
        return this.center;
    }

    public ClubDto setCenter(
            CenterForClubDto[] center) {
        this.center = center;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Double getRating() {
        return this.rating;
    }

    public ClubDto setRating(Double rating) {
        this.rating = rating;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<LocationResponseDto> getLocations() {
        return this.locations;
    }

    public ClubDto setLocations(ArrayList<LocationResponseDto> locations) {
        this.locations = locations;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsApproved() {
        return this.isApproved;
    }

    public ClubDto setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsOnline() {
        return this.isOnline;
    }

    public ClubDto setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Integer getFeedbackCount() {
        return this.feedbackCount;
    }

    public ClubDto setFeedbackCount(Integer feedbackCount) {
        this.feedbackCount = feedbackCount;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<ContactDataResponseDto> getContacts() {
        return this.contacts;
    }

    public ClubDto setContacts(ArrayList<ContactDataResponseDto> contacts) {
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
        ClubDto clubDto = (ClubDto) o;
        return new EqualsBuilder()
                .append(id, clubDto.id)
                .append(ageFrom, clubDto.ageFrom)
                .append(ageTo, clubDto.ageTo)
                .append(name, clubDto.name)
                .append(description, clubDto.description)
                .append(urlWeb, clubDto.urlWeb)
                .append(urlLogo, clubDto.urlLogo)
                .append(urlBackground, clubDto.urlBackground)
                .append(urlGallery, clubDto.urlGallery)
                .append(workTime, clubDto.workTime)
                .append(categories, clubDto.categories)
                .append(user, clubDto.user)
                .append(center, clubDto.center)
                .append(rating, clubDto.rating)
                .append(locations, clubDto.locations)
                .append(isApproved, clubDto.isApproved)
                .append(isOnline, clubDto.isOnline)
                .append(feedbackCount, clubDto.feedbackCount)
                .append(contacts, clubDto.contacts)
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
                ", user=" + Arrays.toString(user) +
                ", center=" + Arrays.toString(center) +
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
            ClubDto.this.id = id;
            return this;
        }

        public Builder withAgeFrom(Integer ageFrom) {
            ClubDto.this.ageFrom = ageFrom;
            return this;
        }

            public Builder withAgeTo(Integer ageTo) {
            ClubDto.this.ageTo = ageTo;
            return this;
        }

        public Builder withName(String name) {
            ClubDto.this.name = name;
            return this;
        }

        public Builder withDescription(String description) {
            ClubDto.this.description = description;
            return this;
        }

        public Builder withUrlWeb(String urlWeb) {
            ClubDto.this.urlWeb = urlWeb;
            return this;
        }

        public Builder withUrlLogo(String urlLogo) {
            ClubDto.this.urlLogo = urlLogo;
            return this;
        }

        public Builder withUrlBackground(String urlBackground) {
            ClubDto.this.urlBackground = urlBackground;
            return this;
        }

        public Builder withUrlGallery(ArrayList<GalleryPhotoDto> urlGallery) {
            ClubDto.this.urlGallery = urlGallery;
            return this;
        }

        public Builder withWorkTime(String workTime) {
            ClubDto.this.workTime = workTime;
            return this;
        }

        public Builder withCategories(ArrayList<CategoryDto> categories) {
            ClubDto.this.categories = categories;
            return this;
        }

        public Builder withUser(UserPreviewDto[] user) {
            ClubDto.this.user = user;
            return this;
        }

        public Builder withCenter(CenterForClubDto[] center) {
            ClubDto.this.center = center;
            return this;
        }

        public Builder withRating(Double rating) {
            ClubDto.this.rating = rating;
            return this;
        }

        public Builder withLocations(ArrayList<LocationResponseDto> locations) {
            ClubDto.this.locations = locations;
            return this;
        }

        public Builder withIsApproved(Boolean isApproved) {
            ClubDto.this.isApproved = isApproved;
            return this;
        }

        public Builder withIsOnline(Boolean isOnline) {
            ClubDto.this.isOnline = isOnline;
            return this;
        }

        public Builder withFeedbackCount(Integer feedbackCount) {
            ClubDto.this.feedbackCount = feedbackCount;
            return this;
        }

        public Builder withContacts(ArrayList<ContactDataResponseDto> contacts) {
            ClubDto.this.contacts = contacts;
            return this;
        }

        public ClubDto build() {
            return ClubDto.this;
        }

    }

}
