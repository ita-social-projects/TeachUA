package com.softserve.teachua.dto.viberbot.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MessageKeyboard {
    @JsonProperty("Type")
    @Builder.Default
    private String type = "keyboard";
    @JsonProperty("DefaultHeight")
    @Builder.Default
    private Boolean defaultHeight = false;
    @JsonProperty("Buttons")
    private List<Button> buttons;
    @JsonProperty("BgColor")
    private String bgColor;
    @JsonProperty("CustomDefaultHeight")
    private String customDefaultHeight;
    @JsonProperty("HeightScale")
    private String heightScale;
    @JsonProperty("ButtonsGroupColumns")
    private String buttonsGroupColumns;
    @JsonProperty("ButtonsGroupRows")
    private String buttonsGroupRows;
    @JsonProperty("InputFieldState")
    private String inputFieldState;
    @JsonProperty("FavoritesMetadata")
    private String favoritesMetadata;

}
