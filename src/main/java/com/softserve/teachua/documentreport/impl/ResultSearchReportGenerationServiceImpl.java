package com.softserve.teachua.documentreport.impl;

import com.softserve.teachua.documentreport.ReportGenerationService;
import com.softserve.teachua.dto.club.ClubResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ResultSearchReportGenerationServiceImpl implements ReportGenerationService<Page<ClubResponse>> {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ResultSearchReportGenerationServiceImpl.class.getName());
    private static final String TEMPLATE_PATH = "/pdf-reports/template/ResultSearchPdfReportTemplate.jrxml";
    private static final String CLUB_SUB_REPORT_TEMPLATE_PATH = "/pdf-reports/template/SubClubReportTemplate.jrxml";

    @Override
    public byte[] getPdfOutput(Page<ClubResponse> clubResponses) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            JasperPrint jasperPrint = createJasperPrint(clubResponses);
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            LOGGER.error("Cannot generate PDF report.", e);
        }
        return new byte[0];
    }

    private JasperPrint createJasperPrint(Page<ClubResponse> clubResponses) throws JRException, IOException {
        try (InputStream inputStream = new FileInputStream(getRealFilePath(TEMPLATE_PATH))) {
            final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            return JasperFillManager.fillReport(jasperReport, getParameters(clubResponses),
                    getDataSource(clubResponses));
        }
    }

    private Map<String, Object> getParameters(Page<ClubResponse> clubResponses) throws IOException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CLUBS", clubResponses.getContent());
        parameters.put("CLUB_SUB_REPORT_TEMPLATE_PATH", getRealFilePath(CLUB_SUB_REPORT_TEMPLATE_PATH));
        return parameters;
    }

    private String getRealFilePath(final String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(Page<ClubResponse> clubResponses) {
        return new JRBeanCollectionDataSource(Collections.singletonList(clubResponses));
    }
}
