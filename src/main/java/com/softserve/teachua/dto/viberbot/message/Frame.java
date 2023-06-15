package com.softserve.teachua.dto.viberbot.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Frame {
    @JsonProperty("BorderWidth")
    @Builder.Default
    private Integer borderWidth = 2;
    @JsonProperty("BorderColor")
    @Builder.Default
    private String borderColor = "#13003b";
    @JsonProperty("CornerRadius")
    @Builder.Default
    private Integer cornerRadius = 10;

}
