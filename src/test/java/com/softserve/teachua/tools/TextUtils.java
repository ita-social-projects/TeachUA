package com.softserve.teachua.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {

    public static String unpackFirstSubText(String regexPattern, String text) {
        String resultText = null;
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            resultText = text.substring(matcher.start(), matcher.end());
        }
        return resultText;
    }

    public static String unpackFirstSubText(String regexPattern, String text, int groupNumber) {
        String resultText = null;
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            resultText = matcher.group(groupNumber);
        }
        return resultText;
    }

    public static String unpackFirstGroupSubText(String regexPattern, String text) {
        return unpackFirstSubText(regexPattern, text, 1);
    }
}
