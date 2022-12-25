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
    private UserPreviewDto user;
    private ArrayList<LocationResponseDto> locations;
    private ArrayList<ContactDataResponseDto> contacts;

    public Integer getId() {
        return this.id;
    }

    public CenterForClubDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CenterForClubDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrlBackgroundPicture() {
        return this.urlBackgroundPicture;
    }

    public CenterForClubDto setUrlBackgroundPicture(String urlBackgroundPicture) {
        this.urlBackgroundPicture = urlBackgroundPicture;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public CenterForClubDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhones() {
        return this.phones;
    }

    public CenterForClubDto setPhones(String phones) {
        this.phones = phones;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public CenterForClubDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getUrlWeb() {
        return this.urlWeb;
    }

    public CenterForClubDto setUrlWeb(String urlWeb) {
        this.urlWeb = urlWeb;
        return this;
    }

    public String getUrlLogo() {
        return this.urlLogo;
    }

    public CenterForClubDto setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
        return this;
    }

    public String getSocialLinks() {
        return this.socialLinks;
    }

    public CenterForClubDto setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
        return this;
    }

    public UserPreviewDto getUser() {
        return this.user;
    }

    public CenterForClubDto setUser(UserPreviewDto user) {
        this.user = user;
        return this;
    }

    public ArrayList<LocationResponseDto> getLocations() {
        return this.locations;
    }

    public CenterForClubDto setLocations(ArrayList<LocationResponseDto> locations) {
        this.locations = locations;
        return this;
    }

    public ArrayList<ContactDataResponseDto> getContacts() {
        return this.contacts;
    }

    public CenterForClubDto setContacts(ArrayList<ContactDataResponseDto> contacts) {
        this.contacts = contacts;
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
