package com.softserve.teachua.utils;

import com.softserve.teachua.dto.club.ClubResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ReportDecorator {

    public static String formDescription(String description) {
        StringBuilder text = new StringBuilder();
        text.append(description);
        text.delete(0, 379);
        return text.substring(0, text.indexOf("type") - 3);
    }

    public static String formYears(Integer ageFrom, Integer ageTo) {
        return "від " + ageFrom + " до " + ageTo + " років";
    }

    public static String getRealFilePath(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    public static String getRealFilePathCategoryLogo(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource("/frontend" + path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }
}

