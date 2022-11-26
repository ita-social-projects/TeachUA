package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

// These interfaces can only be accessible for classes inside the devices package
interface IId {
    IAgeFrom setId(int id);                                 // call setter, set id and call IAgeFrom interface
}

interface IAgeFrom {
    IAgeTo setAgeFrom(int ageFrom);                         // call setter, set ageFrom and call IAgeTo interface
}

interface IAgeTo {
    IName setAgeTo(int ageTo);                              // call setter, set ageTo and call IName interface
}

interface IName {
    IDescription setName(String setName);                   // call setter, set name and call IDescription interface
}

interface IDescription {
    IUrlWeb setDescription(String description);             // call setter, set description and call IUrlWeb interface
}

interface IUrlWeb {
    IUrlLogo setUrlWeb(Object urlWeb);                      // call setter, set urlWeb and call IUrlLogo interface
}

interface IUrlLogo {
    IUrlBackground setUrlLogo(String urlLogo);              // call setter, set urlLogo and call IUrlBackground interface
}

interface IUrlBackground {
    IUrlGallery setUrlBackground(String UrlBackground);     // call setter, set UrlBackground and call IUrlGallery interface
}

interface IUrlGallery {
    IWorkTime setUrlGallery(ArrayList<Object> urlGallery);  // call setter, set urlGallery and call IWorkTime interface
}

interface IWorkTime {
    ICategories setWorkTime(Object workTime);               // call setter, set description and call ICategories interface
}

interface ICategories {
    IUser setCategories(ArrayList<CategoryDto> categories); // call setter, set categories and call IUser interface
}

interface IUser {
    ICenter setUser(Object user);                           // call setter, set user and call ICenter interface
}

interface ICenter {
    IRating setCenter(Object center);                       // call setter, set center and call IRating interface
}

interface IRating {
    ILocations setRating(int rating);                       // call setter, set rating and call ILocations interface
}

interface ILocations {
    IIsApproved setLocations(ArrayList<LocationDto> locations); // call setter, set locations and call IIsApproved interface
}

interface IIsApproved {
    IIsOnline setIsApproved(Object isApproved);             // call setter, set isApproved and call IIsOnline interface
}

interface IIsOnline {
    IFeedbackCount setIsOnline(Object isOnline);            // call setter, set isOnline and call IFeedbackCount interface
}

interface IFeedbackCount {
    IContacts setFeedbackCount(int feedbackCount);          // call setter, set feedbackCount and call IContacts interface
}

interface IContacts {
    IClubDtoBuild setContacts(ArrayList<ContactDto> contacts);  // call setter, set contacts and call IFeedbackCount interface
}

interface IClubDtoBuild {
    ClubDto build();                                        // call setter, build what have been set before
}

public class ClubDto extends BaseDto implements  IId, IAgeFrom, IAgeTo, IName, IDescription, IUrlWeb, IUrlLogo, IUrlBackground, IUrlGallery, IWorkTime, ICategories, IUser, ICenter, IRating, ILocations, IIsApproved, IIsOnline, IFeedbackCount, IContacts, IClubDtoBuild  {

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

    // Constructor (here we need to add only optional fields to initialize them because mandatory field will be
    // initialized anyway)
    private ClubDto() {
    }

    public static IId get() {
        // Declaring that we will return base element But actually we return child
        return new ClubDto();
    }

    public ClubDto build() {
        return this;                                            // method which returns clubDto object to build all
    }

    public int getId() {
        return this.id;
    }

    public IAgeFrom setId(int id) {
        this.id = id;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getAgeFrom() {
        return this.ageFrom;
    }

    public IAgeTo setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getAgeTo() {
        return this.ageTo;
    }

    public IName setAgeTo(int ageTo) {
        this.ageTo = ageTo;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getName() {
        return this.name;
    }

    public IDescription setName(String name) {
        this.name = name;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public IUrlWeb setDescription(String description) {
        this.description = description;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getUrlWeb() {
        return this.urlWeb;
    }

    public IUrlLogo setUrlWeb(Object urlWeb) {
        this.urlWeb = urlWeb;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public IUrlBackground setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public String getUrlBackground() {
        return this.urlBackground;
    }

    public IUrlGallery setUrlBackground(String urlBackground) {
        this.urlBackground = urlBackground;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<Object> getUrlGallery() {
        return this.urlGallery;
    }

    public IWorkTime setUrlGallery(ArrayList<Object> urlGallery) {
        this.urlGallery = urlGallery;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getWorkTime() {
        return this.workTime;
    }

    public ICategories setWorkTime(Object workTime) {
        this.workTime = workTime;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<CategoryDto> getCategories() {
        return this.categories;
    }

    public IUser setCategories(ArrayList<CategoryDto> categories) {
        this.categories = categories;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getUser() {
        return this.user;
    }

    public ICenter setUser(Object user) {
        this.user = user;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getCenter() {
        return this.center;
    }

    public IRating setCenter(Object center) {
        this.center = center;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getRating() {
        return this.rating;
    }

    public ILocations setRating(int rating) {
        this.rating = rating;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<LocationDto> getLocations() {
        return this.locations;
    }

    public IIsApproved setLocations(ArrayList<LocationDto> locations) {
        this.locations = locations;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsApproved() {
        return this.isApproved;
    }

    public IIsOnline setIsApproved(Object isApproved) {
        this.isApproved = isApproved;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public Object getIsOnline() {
        return this.isOnline;
    }

    public IFeedbackCount setIsOnline(Object isOnline) {
        this.isOnline = isOnline;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public int getFeedbackCount() {
        return this.feedbackCount;
    }

    public IContacts setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public ArrayList<ContactDto> getContacts() {
        return this.contacts;
    }

    public IClubDtoBuild setContacts(ArrayList<ContactDto> contacts) {
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
}
