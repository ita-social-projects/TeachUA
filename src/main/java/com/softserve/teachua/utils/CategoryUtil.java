package com.softserve.teachua.utils;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryUtil {
    public static List<String> replaceSemicolonToComma(List<String> categories) {
        return categories != null ? categories.stream()
                .map(categoryName -> categoryName.replaceAll(";", ","))
                .collect(Collectors.toList()) : null;
    }
}
