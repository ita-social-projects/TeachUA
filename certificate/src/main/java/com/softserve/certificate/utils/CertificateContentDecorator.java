package com.softserve.certificate.utils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CertificateContentDecorator {
    public static final String PATTERN_WITHOUT_MONTH_AND_YEAR = "з d";
    public static final String PATTERN_WITHOUT_YEAR = "з d MMMM";
    public static final String PATTERN_WITH_MONTH_AND_YEAR = " по d MMMM yyyy року";

    public String formHours(Integer hours) {
        return String.valueOf(hours);
    }

    public String formDates(LocalDate startDate, LocalDate endDate) {
        Locale locale = Locale.forLanguageTag("uk-UA");

        DateTimeFormatter startFormatter;
        DateTimeFormatter endFormatter;

        if (startDate.getMonth().getValue() == endDate.getMonth().getValue()) {
            startFormatter = DateTimeFormatter.ofPattern(PATTERN_WITHOUT_MONTH_AND_YEAR, locale);
            endFormatter = DateTimeFormatter.ofPattern(PATTERN_WITH_MONTH_AND_YEAR, locale);

            return startFormatter.format(startDate) + endFormatter.format(endDate);
        } else {
            startFormatter = DateTimeFormatter.ofPattern(PATTERN_WITHOUT_YEAR, locale);
            endFormatter = DateTimeFormatter.ofPattern(PATTERN_WITH_MONTH_AND_YEAR, locale);
        }

        return startFormatter.format(startDate) + endFormatter.format(endDate);
    }

    /**
     *  This method is used in .jrxml certificate templates to integrate images into pdf.
     *
     * @param path {@code String}
     * @return full system path to image
     * @throws IOException if something occurred with file
     */
    public static String getRealPathToImage(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource("/certificates/images/" + path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    public String getRealFilePath(String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }
}
