package com.softserve.edu.testcases.repositories.club;

// These interfaces can only be accessible for classes inside the devices package
interface ITitle {
    ICategory setTitle(String title);                           // call setter, set title and call ICategory interface
}

interface ICategory {
    IDescription setCategory(String category);                  // call setter, set category and call IDescription interface
}

interface IDescription {
    IRate setDescription(String description);                   // call setter, set description and call IRate interface
}

interface IRate {
    ILocation setRate(int rate);                                // call setter, set rate and call ILocation interface
}

interface ILocation {
        IClubBuild setLocation(String location);                // call setter, set location and call IClubBuild interface
}

interface IClubBuild {
    Club build();                                               // call setter, build what have been set before
}

public class Club implements ITitle, ICategory, IDescription, IRate, ILocation, IClubBuild {

    // Club fields
    private String title;                                       // club title
    private String category;                                    // club category
    private String description;                                 // club description
    private int rate;                                           // club rate
    private String location;                                    // club location

    // Constructor (here we need to add only optional fields to initialize them because mandatory field will be
    // initialized anyway)
    private Club() {
    }

    public static ITitle get() {
        // Declaring that we will return base element But actually we return child
        return new Club();
    }

    // Setters

    public ICategory setTitle(String title) {
        this.title = title;                                     // set title
        // Methods are invoked on objects, this refers to the object on which the current method is called
        return this;
    }

    public IDescription setCategory(String category) {
        this.category = category;                               // set category
        return this;
    }

    public IRate setDescription(String description) {
        this.description = description;                         // set description
        return this;
    }

    public ILocation setRate(int rate) {
        this.rate = rate;                                       // set rate
        return this;
    }

    public IClubBuild setLocation(String location) {
        this.location = location;                               // set location
        return this;
    }

    public Club build() {
        return this;                                            // method which returns club object to build all
    }

    // Getters

    public String getTitle() {
        return title;                                           // get title
    }

    public String getCategory() {
        return category;                                        // get category
    }

    public String getDescription() {
        return description;                                     // get description
    }

    public int getRate() {
        return rate;                                            // get rate
    }

    public String getLocation() {
        return location;                                        // get location
    }

    // Get values of this class
    @Override
    public String toString() {
        return "Club{" +
                "title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", rate=" + rate +
                ", location='" + location + '\'' +
                '}';
    }
}
