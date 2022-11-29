package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

public class ClubDto extends BaseDto {

    private int id;
    private int ageFrom;
    private int ageTo;
    private String name;
    private String description;
    private Object urlWeb;
    private String urlLogo;
    private String urlBackground;
    private ArrayList<Object> urlGallery;
    private Object workTime;
    private ArrayList<CategoryDto> categories;
    private Object user;
    private Object center;
    private int rating;
    private ArrayList<LocationDto> locations;
    private Object isApproved;
    private Object isOnline;
    private int feedbackCount;
    private ArrayList<ContactDto> contacts;

    /*
     * Method using for build new ClubDto payload
     */
    public static Builder newBuilder() {
        return new ClubDto().new Builder();
    }

    public int getId() {
        return this.id;
    }

    public ClubDto setId(int id) {
        this.id = id;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getAgeFrom() {
        return this.ageFrom;
    }

    public ClubDto setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getAgeTo() {
        return this.ageTo;
    }

    public ClubDto setAgeTo(int ageTo) {
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

    public ClubDto setUrlWeb(Object urlWeb) {
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

    public ArrayList<Object> getUrlGallery() {
        return this.urlGallery;
    }

    public ClubDto setUrlGallery(ArrayList<Object> urlGallery) {
        this.urlGallery = urlGallery;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getWorkTime() {
        return this.workTime;
    }

    public ClubDto setWorkTime(Object workTime) {
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

    public ClubDto setUser(Object user) {
        this.user = user;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getCenter() {
        return this.center;
    }

    public ClubDto setCenter(Object center) {
        this.center = center;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getRating() {
        return this.rating;
    }

    public ClubDto setRating(int rating) {
        this.rating = rating;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<LocationDto> getLocations() {
        return this.locations;
    }

    public ClubDto setLocations(ArrayList<LocationDto> locations) {
        this.locations = locations;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsApproved() {
        return this.isApproved;
    }

    public ClubDto setIsApproved(Object isApproved) {
        this.isApproved = isApproved;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsOnline() {
        return this.isOnline;
    }

    public ClubDto setIsOnline(Object isOnline) {
        this.isOnline = isOnline;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getFeedbackCount() {
        return this.feedbackCount;
    }

    public ClubDto setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<ContactDto> getContacts() {
        return this.contacts;
    }

    public ClubDto setContacts(ArrayList<ContactDto> contacts) {
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

        public Builder withId(int id) {
            ClubDto.this.id = id;
            return this;
        }

        public Builder withAgeFrom(int ageFrom) {
            ClubDto.this.ageFrom = ageFrom;
            return this;
        }

        public Builder withAgeTo(int ageTo) {
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

        public Builder withUrlWeb(Object urlWeb) {
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

        public Builder withUrlGallery(ArrayList<Object> urlGallery) {
            ClubDto.this.urlGallery = urlGallery;
            return this;
        }

        public Builder withWorkTime(Object workTime) {
            ClubDto.this.workTime = workTime;
            return this;
        }

        public Builder withCategories(ArrayList<CategoryDto> categories) {
            ClubDto.this.categories = categories;
            return this;
        }

        public Builder withUser(Object user) {
            ClubDto.this.user = user;
            return this;
        }

        public Builder withCenter(Object center) {
            ClubDto.this.center = center;
            return this;
        }

        public Builder withRating(int rating) {
            ClubDto.this.rating = rating;
            return this;
        }

        public Builder withLocations(ArrayList<LocationDto> locations) {
            ClubDto.this.locations = locations;
            return this;
        }

        public Builder withIsApproved(Object isApproved) {
            ClubDto.this.isApproved = isApproved;
            return this;
        }

        public Builder withIsOnline(Object isOnline) {
            ClubDto.this.isOnline = isOnline;
            return this;
        }

        public Builder withFeedbackCount(int feedbackCount) {
            ClubDto.this.feedbackCount = feedbackCount;
            return this;
        }

        public Builder withContacts(ArrayList<ContactDto> contacts) {
            ClubDto.this.contacts = contacts;
            return this;
        }

        public ClubDto build() {
            return ClubDto.this;
        }

    }

}
