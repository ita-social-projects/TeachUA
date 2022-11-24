package com.softserve.edu.models;

import com.softserve.edu.utils.DtoConverter;

public abstract class BaseDto {

    public String toJsonString() {
        return DtoConverter.dtoToJsonString(this);
    }

    public String toPrettyJsonString() {
        return DtoConverter.dtoToPrettyJsonString(this);
    }

    @Override
    public String toString() {
        return toJsonString();
    }

}
