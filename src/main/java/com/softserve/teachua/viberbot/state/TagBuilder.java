package com.softserve.teachua.viberbot.state;

import j2html.TagCreator;

public class TagBuilder {

    private TagBuilder() {
        throw new UnsupportedOperationException("This class should be used statically");
    }

    public static String font(String color, String text) {
        return TagCreator.tag("font")
                .attr("color", color)
                .withText(text)
                .render();
    }

    public static String defaultColor(String text) {
        return font("#13003b", text);
    }
}
