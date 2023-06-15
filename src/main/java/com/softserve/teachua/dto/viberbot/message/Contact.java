package com.softserve.teachua.dto.viberbot.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Contact {
    @JsonProperty("phone_number")
    private String phoneNumber;
}
