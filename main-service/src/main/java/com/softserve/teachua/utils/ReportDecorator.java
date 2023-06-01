package com.softserve.teachua.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ReportDecorator {
    private ReportDecorator() {
    }

    public static String formDescription(String description) {
        final JSONObject result = new JSONObject(description);
        final JSONArray resultArray = result.getJSONArray("blocks");
        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject obj = resultArray.getJSONObject(i);
            String text = obj.getString("text");
            if (!text.isEmpty() && text.length() > 5) {
                return text;
            }
        }
        return "Опис відсутній";
    }

    public static String formYears(Integer ageFrom, Integer ageTo) {
        return "від " + ageFrom + " до " + ageTo + " років";
    }

    public static String getRealFilePath(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    public static String getRealFilePathFrontend(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource("/frontend" + path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }
}
