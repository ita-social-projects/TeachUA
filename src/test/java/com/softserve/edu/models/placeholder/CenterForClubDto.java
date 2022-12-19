package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;

public class CenterForClubDto extends BaseDto {

    private Integer id;
    private String name;
    private String urlBackgroundPicture;
    private String email;
    private String phones;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String socialLinks;
    private UserPreviewDto[] user;
    private ArrayList<LocationResponseDto> locations;
    private ArrayList<ContactDataResponseDto> contacts;

    public int getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlBackgroundPicture() {
        return this.urlBackgroundPicture;
    }

    public void setUrlBackgroundPicture(String urlBackgroundPicture) {
        this.urlBackgroundPicture = urlBackgroundPicture;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhones() {
        return this.phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlWeb() {
        return this.urlWeb;
    }

    public void setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getSocialLinks() {
        return this.socialLinks;
    }

    public void setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
    }

    public UserPreviewDto[] getUser() {
        return this.user;
    }

    public void setUser(UserPreviewDto[] user) {
        this.user = user;
    }

    public ArrayList<LocationResponseDto> getLocations() {
        return this.locations;
    }

    public void setLocations(ArrayList<LocationResponseDto> locations) {
        this.locations = locations;
    }

    public ArrayList<ContactDataResponseDto> getContacts() {
        return this.contacts;
    }

    public void setContacts(ArrayList<ContactDataResponseDto> contacts) {
        this.contacts = contacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CenterForClubDto centerForClubDto = (CenterForClubDto) o;
        return new EqualsBuilder()
                .append(id, centerForClubDto.id)
                .append(name, centerForClubDto.name)
                .append(urlBackgroundPicture, centerForClubDto.urlBackgroundPicture)
                .append(email, centerForClubDto.email)
                .append(phones, centerForClubDto.phones)
                .append(description, centerForClubDto.description)
                .append(urlWeb, centerForClubDto.urlWeb)
                .append(urlLogo, centerForClubDto.urlLogo)
                .append(socialLinks, centerForClubDto.socialLinks)
                .append(user, centerForClubDto.user)
                .append(locations, centerForClubDto.locations)
                .append(contacts, centerForClubDto.contacts)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(urlBackgroundPicture)
                .append(email)
                .append(phones)
                .append(description)
                .append(urlWeb)
                .append(urlLogo)
                .append(socialLinks)
                .append(user)
                .append(locations)
                .append(contacts)
                .toHashCode();
    }
}
