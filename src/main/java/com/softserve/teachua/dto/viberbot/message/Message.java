package com.softserve.teachua.dto.viberbot.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Message {
    private String receiver;
    private String text;
    @Builder.Default
    private String type = "text";
    private MessageKeyboard keyboard;
    private Contact contact;
    private String media;
    @JsonProperty("min_api_version")
    @Builder.Default
    private int minApiVersion = 6;
}

