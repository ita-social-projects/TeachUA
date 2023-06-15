package com.softserve.teachua.dto.viberbot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Sender {
    private String id;
    private String name;
    private String avatar;
    private String language;
    private String country;
    @JsonProperty("api_version")
    private Integer apiVersion;
}
