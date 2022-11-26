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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityName() {
        return this.cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Object getStationName() {
        return this.stationName;
    }

    public void setStationName(Object stationName) {
        this.stationName = stationName;
    }

    public LocationCityDto getLocationCity() {
        return this.locationCity;
    }

    public void setLocationCity(LocationCityDto locationCity) {
        this.locationCity = locationCity;
    }

    public int getCityId() {
        return this.cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getDistrictId() {
        return this.districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public Object getStationId() {
        return this.stationId;
    }

    public void setStationId(Object stationId) {
        this.stationId = stationId;
    }

    public int getClubId() {
        return this.clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public Object getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Object getPhone() {
        return this.phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
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
