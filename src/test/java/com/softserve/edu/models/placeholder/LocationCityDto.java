package com.softserve.edu.models.placeholder;

import com.softserve.edu.models.BaseDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LocationCityDto extends BaseDto {

    private Integer id;
    private String name;
    private Double latitude;
    private Double longitude;

    public Integer getId() {
        return this.id;
    }

    public LocationCityDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public LocationCityDto setName(String name) {
        this.name = name;
        return this;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public LocationCityDto setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public LocationCityDto setLongitude(Double longitude) {
        this.longitude = longitude;
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
        LocationCityDto locationCityDto = (LocationCityDto) o;
        return new EqualsBuilder()
                .append(id, locationCityDto.id)
                .append(name, locationCityDto.name)
                .append(latitude, locationCityDto.latitude)
                .append(longitude, locationCityDto.longitude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }
}
