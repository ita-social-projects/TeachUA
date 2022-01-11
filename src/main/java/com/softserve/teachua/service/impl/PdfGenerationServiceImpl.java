package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.service.PdfGenerationService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService<FeedbackResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerationServiceImpl.class.getName());
    private static final String TEMPLATE_PATH = "/pdf-reports/template/FeedbackTemplate.jrxml";
    private static final String IMAGE_PATH = "/pdf-reports/image/";
    private static final String IMAGE_PATH_BALLS = "club/balls.jpg";
    private static final String IMAGE_PATH_EXERCISE = "club/exercise.jpg";
    private static final String IMAGE_PATH_KIDS_JUMP = "club/kids_jump.png";
    private static final String IMAGE_PATH_PENCILS = "club/pencils.jpg";

    public byte[] getPdfOutput(final FeedbackResponse feedbackResponse) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JasperPrint jasperPrint = createJasperPrint(feedbackResponse);
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            LOGGER.error("Cannot generate PDF report.", e);
        }
        return new byte[0];
    }

    private JasperPrint createJasperPrint(FeedbackResponse feedbackResponse) throws JRException, IOException {
        try (InputStream inputStream = new FileInputStream(getRealFilePath(TEMPLATE_PATH))) {
            final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            return JasperFillManager.fillReport(jasperReport, getParameters(feedbackResponse), getDataSource(feedbackResponse));
        }
    }

    private Map<String, Object> getParameters(FeedbackResponse feedbackResponse) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CONTEXT_IMAGE", getRealFilePath(IMAGE_PATH));
        parameters.put("logo", getRealFilePath(IMAGE_PATH + getCategoryImageName(feedbackResponse)));
        parameters.put("categories", getCategories(feedbackResponse));
        parameters.put("category-color", getCategoryColor(feedbackResponse));
        parameters.put("location", getLocationAddress(feedbackResponse));
        parameters.put("locationImage", getLocationImage());
        parameters.put("years", getYears(feedbackResponse));
        parameters.put("contacts", getContacts(feedbackResponse));
        parameters.put("description", getDescription(feedbackResponse));
        parameters.put("img-balls", getClubImage(IMAGE_PATH_BALLS));
        parameters.put("img-exercise", getClubImage(IMAGE_PATH_EXERCISE));
        parameters.put("img-kids-jump", getClubImage(IMAGE_PATH_KIDS_JUMP));
        parameters.put("img-pencils", getClubImage(IMAGE_PATH_PENCILS));
        return parameters;
    }

    private String getClubImage(String imagePath) throws IOException {
        return getRealFilePath(IMAGE_PATH + imagePath);
    }

    private String getDescription(FeedbackResponse feedbackResponse) {
        StringBuilder description = new StringBuilder();
        description.append(feedbackResponse.getClub().getDescription());
        description.delete(0, 379);
        return description.substring(0, description.indexOf("type") - 3);
    }

    private String getContacts(FeedbackResponse feedbackResponse) {
        return feedbackResponse.getClub().getContacts();
    }

    private String getYears(FeedbackResponse feedbackResponse) {
        return "від " + feedbackResponse.getClub().getAgeFrom() +
                " до " + feedbackResponse.getClub().getAgeTo() + " років";
    }

    private String getLocationImage() throws IOException {
        return getRealFilePath(IMAGE_PATH + "cluster.png");
    }

    private String getLocationAddress(FeedbackResponse feedbackResponse) {
        Optional<Location> location = feedbackResponse.getClub().getLocations().stream().findFirst();
        if (location.isPresent()) {
            return location.get().getAddress();
        } else {
            LOGGER.error("Cannot find location");
            return "";
        }
    }

    private Set<String> getCategories(FeedbackResponse feedbackResponse) {
        return feedbackResponse.getClub().getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
    }

    private String getCategoryColor(FeedbackResponse feedbackResponse) {
        Optional<Category> category = feedbackResponse.getClub().getCategories().stream().findFirst();
        if (category.isPresent()) {
            return category.get().getBackgroundColor();
        }
        LOGGER.error("Cannot find the category color");
        return "";
    }

    private String getCategoryImageName(FeedbackResponse feedbackResponse) {
        Optional<Category> category = feedbackResponse.getClub().getCategories().stream().findFirst();
        if (category.isPresent()) {
            String urlLogo = category.get().getUrlLogo();
            return urlLogo.substring(urlLogo.indexOf("categories/") + 11);
        } else {
            LOGGER.error("Cannot find the category image");
            return "";
        }
    }

    private String getRealFilePath(final String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(FeedbackResponse feedbackResponse) {
        return new JRBeanCollectionDataSource(Collections.singletonList(feedbackResponse));
    }
}