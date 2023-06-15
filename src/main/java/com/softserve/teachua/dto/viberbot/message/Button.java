package com.softserve.teachua.dto.viberbot.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Button {
    @JsonProperty("ActionType")
    @Builder.Default
    private String actionType = "reply";
    @JsonProperty("TextSize")
    @Builder.Default
    private String textSize = "large";
    @JsonProperty("BgColor")
    @Builder.Default
    private String bgColor = "#f0f0f0";
    @JsonProperty("TextVAlign")
    @Builder.Default
    private String textVAlign = "middle";
    @JsonProperty("TextHAlign")
    @Builder.Default
    private String textHAlign = "center";
    @JsonProperty("TextShouldFit")
    @Builder.Default
    private boolean textShouldFit = false;
    @JsonProperty("Silent")
    @Builder.Default
    private boolean silent = false;
    @JsonProperty("Columns")
    @Builder.Default
    private Integer columns = 3;
    @JsonProperty("Rows")
    @Builder.Default
    private Integer rows = 1;
    @JsonProperty("ActionBody")
    private String actionBody;
    @JsonProperty("Text")
    private String text;
    @JsonProperty("BgLoop")
    private boolean bgLoop;
    @JsonProperty("TextOpacity")
    private Integer textOpacity;
    @JsonProperty("Image")
    private String image;
    @JsonProperty("BgMediaType")
    private String bgMediaType;
    @JsonProperty("BgMedia")
    private String bgMedia;
    @JsonProperty("BgMediaScaleType")
    private String bgMediaScaleType;
    @JsonProperty("ImageScaleType")
    private String imageScaleType;
    @JsonProperty("TextPaddings")
    private List<Integer> textPaddings;
    @JsonProperty("TextBgGradientColor")
    private Integer textBgGradientColor;
    @JsonProperty("HeightScale")
    private Integer heightScale;
    @JsonProperty("Frame")
    private Frame frame;
    @JsonProperty("OpenURLType")
    private String openURLType;
}

