package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

public class LocationDto extends BaseDto {

    private int id;
    private String name;
    private String address;
    private String cityName;
    private String districtName;
    private Object stationName;
    private LocationCityDto locationCity;
    private int cityId;
    private int districtId;
    private Object stationId;
    private int clubId;
    private Object coordinates;
    private double longitude;
    private double latitude;
    private Object phone;

    public int getId() {
        return this.id;
    }

    public LocationDto setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LocationDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public LocationDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCityName() {
        return this.cityName;
    }

    public LocationDto setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public LocationDto setDistrictName(String districtName) {
        this.districtName = districtName;
        return this;
    }

    public Object getStationName() {
        return this.stationName;
    }

    public LocationDto setStationName(Object stationName) {
        this.stationName = stationName;
        return this;
    }

    public LocationCityDto getLocationCity() {
        return this.locationCity;
    }

    public LocationDto setLocationCity(LocationCityDto locationCity) {
        this.locationCity = locationCity;
        return this;
    }

    public int getCityId() {
        return this.cityId;
    }

    public LocationDto setCityId(int cityId) {
        this.cityId = cityId;
        return this;
    }

    public int getDistrictId() {
        return this.districtId;
    }

    public LocationDto setDistrictId(int districtId) {
        this.districtId = districtId;
        return this;
    }

    public Object getStationId() {
        return this.stationId;
    }

    public LocationDto setStationId(Object stationId) {
        this.stationId = stationId;
        return this;
    }

    public int getClubId() {
        return this.clubId;
    }

    public LocationDto setClubId(int clubId) {
        this.clubId = clubId;
        return this;
    }

    public Object getCoordinates() {
        return this.coordinates;
    }

    public LocationDto setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public LocationDto setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public LocationDto setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Object getPhone() {
        return this.phone;
    }

    public LocationDto setPhone(Object phone) {
        this.phone = phone;
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
        LocationDto locationDto = (LocationDto) o;
        return new EqualsBuilder()
                .append(id, locationDto.id)
                .append(name, locationDto.name)
                .append(address, locationDto.address)
                .append(cityName, locationDto.cityName)
                .append(districtName, locationDto.districtName)
                .append(stationName, locationDto.stationName)
                .append(locationCity, locationDto.locationCity)
                .append(cityId, locationDto.cityId)
                .append(districtId, locationDto.districtId)
                .append(stationId, locationDto.stationId)
                .append(clubId, locationDto.clubId)
                .append(coordinates, locationDto.coordinates)
                .append(longitude, locationDto.longitude)
                .append(latitude, locationDto.latitude)
                .append(phone, locationDto.phone)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(address)
                .append(cityName)
                .append(districtName)
                .append(stationName)
                .append(locationCity)
                .append(cityId)
                .append(districtId)
                .append(stationId)
                .append(clubId)
                .append(coordinates)
                .append(longitude)
                .append(latitude)
                .append(phone)
                .toHashCode();
    }
}
