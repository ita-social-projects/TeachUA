package com.softserve.certificate.constants;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    WARNING("1"),
    ERROR("2"),
    SUCCESS("3");

    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}
