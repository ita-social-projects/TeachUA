package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LocationResponseDto extends BaseDto {

    private Integer id;
    private String name;
    private String address;
    private String cityName;
    private String districtName;
    private String stationName;
    private CityResponseDto locationCity;
    private Integer cityId;
    private Integer districtId;
    private Integer stationId;
    private Integer clubId;
    private String coordinates;
    private Double longitude;
    private Double latitude;
    private String phone;

    public int getId() {
        return this.id;
    }

    public LocationResponseDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LocationResponseDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public LocationResponseDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getCityName() {
        return this.cityName;
    }

    public LocationResponseDto setCityName(String cityName) {
        this.cityName = cityName;
        return this;
    }

    public String getDistrictName() {
        return this.districtName;
    }

    public LocationResponseDto setDistrictName(String districtName) {
        this.districtName = districtName;
        return this;
    }

    public Object getStationName() {
        return this.stationName;
    }

    public LocationResponseDto setStationName(String stationName) {
        this.stationName = stationName;
        return this;
    }

    public CityResponseDto getLocationCity() {
        return this.locationCity;
    }

    public LocationResponseDto setLocationCity(CityResponseDto locationCity) {
        this.locationCity = locationCity;
        return this;
    }

    public int getCityId() {
        return this.cityId;
    }

    public LocationResponseDto setCityId(Integer cityId) {
        this.cityId = cityId;
        return this;
    }

    public int getDistrictId() {
        return this.districtId;
    }

    public LocationResponseDto setDistrictId(Integer districtId) {
        this.districtId = districtId;
        return this;
    }

    public Object getStationId() {
        return this.stationId;
    }

    public LocationResponseDto setStationId(Integer stationId) {
        this.stationId = stationId;
        return this;
    }

    public int getClubId() {
        return this.clubId;
    }

    public LocationResponseDto setClubId(Integer clubId) {
        this.clubId = clubId;
        return this;
    }

    public Object getCoordinates() {
        return this.coordinates;
    }

    public LocationResponseDto setCoordinates(String coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public LocationResponseDto setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public LocationResponseDto setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Object getPhone() {
        return this.phone;
    }

    public LocationResponseDto setPhone(String phone) {
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
        LocationResponseDto locationResponseDto = (LocationResponseDto) o;
        return new EqualsBuilder()
                .append(id, locationResponseDto.id)
                .append(name, locationResponseDto.name)
                .append(address, locationResponseDto.address)
                .append(cityName, locationResponseDto.cityName)
                .append(districtName, locationResponseDto.districtName)
                .append(stationName, locationResponseDto.stationName)
                .append(locationCity, locationResponseDto.locationCity)
                .append(cityId, locationResponseDto.cityId)
                .append(districtId, locationResponseDto.districtId)
                .append(stationId, locationResponseDto.stationId)
                .append(clubId, locationResponseDto.clubId)
                .append(coordinates, locationResponseDto.coordinates)
                .append(longitude, locationResponseDto.longitude)
                .append(latitude, locationResponseDto.latitude)
                .append(phone, locationResponseDto.phone)
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
