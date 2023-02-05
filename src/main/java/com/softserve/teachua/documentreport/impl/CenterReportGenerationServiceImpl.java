package com.softserve.teachua.documentreport.impl;

import com.softserve.teachua.documentreport.ReportGenerationService;
import com.softserve.teachua.dto.center.CenterResponse;
import com.softserve.teachua.service.CenterService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CenterReportGenerationServiceImpl implements ReportGenerationService<CenterResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterReportGenerationServiceImpl.class.getName());
    private static final String CENTER_TEMPLATE_PATH = "/pdf-reports/template/CenterPdfReportTemplate.jrxml";
    private static final String CLUBS_SUB_REPORT_PATH = "/pdf-reports/template/SubClubReportTemplate.jrxml";
    private static final String CONTACTS_SUB_REPORT_PATH = "/pdf-reports/template/ContactsSubReportTemplate.jrxml";

    private final CenterService centerService;

    @Autowired
    public CenterReportGenerationServiceImpl(final CenterService centerService) {
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
        InputStream inputStream = new FileInputStream(getRealFilePath(CENTER_TEMPLATE_PATH));
        final JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        return JasperFillManager.fillReport(jasperReport, getParameters(centerResponse), getDataSource(centerResponse));
    }

    private Map<String, Object> getParameters(CenterResponse centerResponse) throws IOException, JRException {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CENTER", centerResponse);
        parameters.put("CLUBS_SUB_REPORT_PATH", getRealFilePath(CLUBS_SUB_REPORT_PATH));
        parameters.put("CONTACTS_SUB_REPORT_PATH", getRealFilePath(CONTACTS_SUB_REPORT_PATH));
        parameters.put("CONTACTS", centerService.getCenterProfileById(centerResponse.getId()).getContacts());
        return parameters;
    }

    private String getRealFilePath(final String path) throws IOException {
        Path resourcePath = Paths.get((new ClassPathResource(path)).getURI());
        return resourcePath.toFile().getAbsolutePath();
    }

    private JRDataSource getDataSource(CenterResponse centerResponse) {
        return new JRBeanCollectionDataSource(Collections.singletonList(centerResponse));
    }
}
