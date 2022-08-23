package com.softserve.teachua.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CertificateContentDecorator {
    public final static String PATTERN_WITHOUT_MONTH_AND_YEAR = "з d";
    public final static String PATTERN_WITHOUT_YEAR = "з d MMMM";
    public final static String PATTERN_WITH_MONTH_AND_YEAR = " до d MMMM yyyy року";

    public String formHours(Integer hours){
        return "Тривалість навчання - " + hours + " годин.";
    }
    public String formDates(LocalDate startDate, LocalDate endDate) {
        Locale locale = Locale.forLanguageTag("uk-UA");

        DateTimeFormatter startFormatter = null;
        DateTimeFormatter endFormatter = null;

        if (startDate.getMonth().getValue() == endDate.getMonth().getValue()){
            startFormatter = DateTimeFormatter.ofPattern(PATTERN_WITHOUT_MONTH_AND_YEAR, locale);
            endFormatter = DateTimeFormatter.ofPattern(PATTERN_WITH_MONTH_AND_YEAR, locale);

            return startFormatter.format(startDate) + endFormatter.format(endDate);
        }else{
            startFormatter = DateTimeFormatter.ofPattern(PATTERN_WITHOUT_YEAR, locale);
            endFormatter = DateTimeFormatter.ofPattern(PATTERN_WITH_MONTH_AND_YEAR, locale);
        }

        return startFormatter.format(startDate) + endFormatter.format(endDate);
    }

    public static String getRealPathToImage(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource("/certificates/images/" + path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    public String getRealFilePath(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }
}
