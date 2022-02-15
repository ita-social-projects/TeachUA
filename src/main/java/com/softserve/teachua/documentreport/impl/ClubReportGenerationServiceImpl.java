package com.softserve.teachua.documentreport.impl;

import com.softserve.teachua.documentreport.ReportGenerationService;
import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.service.ClubService;
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
public class ClubReportGenerationServiceImpl implements ReportGenerationService<ClubResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClubReportGenerationServiceImpl.class.getName());
    private static final String TEMPLATE_PATH = "/pdf-reports/template/ClubPdfReportTemplate.jrxml";
    private static final String LOCATION_SUB_REPORT_PATH = "/pdf-reports/template/LocationsSubReportTemplate.jrxml";
    private static final String CATEGORIES_SUB_REPORT_PATH = "/pdf-reports/template/CategoriesSubReportTemplate.jrxml";
    private static final String CONTACTS_SUB_REPORT_PATH = "/pdf-reports/template/ContactsSubReportTemplate.jrxml";

    private final ClubService clubService;

    @Autowired
    public ClubReportGenerationServiceImpl(final ClubService clubService) {
        this.clubService = clubService;
    }

    public byte[] getPdfOutput(final ClubResponse clubResponse) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JasperPrint jasperPrint = createJasperPrint(clubResponse);
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            LOGGER.error("Cannot generate PDF report.", e);
        }
        return new byte[0];
    }

    private JasperPrint createJasperPrint(ClubResponse clubResponse) throws JRException, IOException {
        try (InputStream inputStream = new FileInputStream(getRealFilePath(TEMPLATE_PATH))) {
            final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            return JasperFillManager.fillReport(jasperReport, getParameters(clubResponse), getDataSource(clubResponse));
        }
    }

    private Map<String, Object> getParameters(ClubResponse clubResponse) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CLUB", clubService.getClubByName(clubResponse.getName()));
        parameters.put("LOCATION_SUB_REPORT_PATH", getRealFilePath(LOCATION_SUB_REPORT_PATH));
        parameters.put("CATEGORIES_SUB_REPORT_PATH", getRealFilePath(CATEGORIES_SUB_REPORT_PATH));
        parameters.put("CONTACTS_SUB_REPORT_PATH", getRealFilePath(CONTACTS_SUB_REPORT_PATH));
        return parameters;
    }

    private String getRealFilePath(final String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(ClubResponse clubResponse) {
        return new JRBeanCollectionDataSource(Collections.singletonList(clubResponse));
    }
}