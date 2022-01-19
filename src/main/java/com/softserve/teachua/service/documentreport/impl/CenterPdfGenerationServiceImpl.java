package com.softserve.teachua.service.documentreport.impl;

import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.service.CenterService;
import com.softserve.teachua.service.documentreport.PdfGenerationService;
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
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class CenterPdfGenerationServiceImpl implements PdfGenerationService<CenterResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterPdfGenerationServiceImpl.class.getName());
    private static final String TEMPLATE_PATH = "/pdf-reports/template/CenterPdfReportTemplate.jrxml";
    private static final String IMAGE_PATH = "/pdf-reports/image/";
    private static final String LOGO_PATH = "/pdf-reports/image/center/illustration-logo.png";
    private static final String IMAGE_PATH_BALLS = "club/balls.jpg";
    private static final String IMAGE_PATH_EXERCISE = "club/exercise.jpg";
    private static final String IMAGE_PATH_KIDS_JUMP = "club/kids_jump.png";
    private static final String IMAGE_PATH_PENCILS = "club/pencils.jpg";

    private final CenterService centerService;

    @Autowired
    public CenterPdfGenerationServiceImpl(final CenterService centerService) {
        this.centerService = centerService;
    }

    @Override
    public byte[] getPdfOutput(final CenterResponse centerResponse) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JasperPrint jasperPrint = createJasperPrint(centerResponse);
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            LOGGER.error("Cannot generate PDF report.", e);
        }
        return new byte[0];
    }

    private JasperPrint createJasperPrint(CenterResponse centerResponse) throws JRException, IOException {
        try (InputStream inputStream = new FileInputStream(getRealFilePath(TEMPLATE_PATH))) {
            final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            return JasperFillManager.fillReport(jasperReport, getParameters(centerResponse), getDataSource(centerResponse));
        }
    }

    private Map<String, Object> getParameters(CenterResponse centerResponse) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("center", getCenter(centerResponse.getName()));
        parameters.put("CONTEXT_IMAGE", getRealFilePath(IMAGE_PATH));
        parameters.put("logo", getRealFilePath(LOGO_PATH));
        parameters.put("locationImage", getLocationImage());
        parameters.put("contacts", getContacts(centerResponse));
        parameters.put("description", getDescription(centerResponse));
        parameters.put("img-balls", getCenterImage(IMAGE_PATH_BALLS));
        parameters.put("img-exercise", getCenterImage(IMAGE_PATH_EXERCISE));
        parameters.put("img-kids-jump", getCenterImage(IMAGE_PATH_KIDS_JUMP));
        parameters.put("img-pencils", getCenterImage(IMAGE_PATH_PENCILS));
        return parameters;
    }

    private String getCenterImage(String imagePath) throws IOException {
        return getRealFilePath(IMAGE_PATH + imagePath);
    }

    private String getDescription(CenterResponse centerResponse) {
        return getCenter(centerResponse.getName()).getDescription();
    }

    private String getContacts(CenterResponse centerResponse) {
        return getCenter(centerResponse.getName()).getContacts();
    }

    private String getLocationImage() throws IOException {
        return getRealFilePath(IMAGE_PATH + "cluster.png");
    }


    private String getRealFilePath(final String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(CenterResponse centerResponse) {
        return new JRBeanCollectionDataSource(Collections.singletonList(centerResponse));
    }

    private Center getCenter(String name) {
        return centerService.getCenterByName(name);
    }
}
