package com.softserve.edu.models.enums;

public enum HttpStatus {

    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION(203, "Non-Authoritative Information"),
    NO_CONTENT(204, "No Content"),

    MULTIPLE_CHOICES(300, "Multiple Choices"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String desc;

    // Constructor in enum always should be private (if we do not write anything, by default it's private)
    HttpStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }

    public int toInteger() {
        return this.code;
    }

}
