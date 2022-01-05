package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.feedback.FeedbackResponse;
import com.softserve.teachua.model.Category;
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

@Service
public class PdfGenerationServiceImpl implements PdfGenerationService<FeedbackResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGenerationServiceImpl.class.getName());
    private static final String TEMPLATE_PATH = "/pdf-reports/template/FeedbackTemplate.jrxml";
    private static final String IMAGE_PATH = "/pdf-reports/image/";

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
        parameters.put("logo", getRealFilePath(IMAGE_PATH + getCategoryImageName(feedbackResponse)));
        return parameters;
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