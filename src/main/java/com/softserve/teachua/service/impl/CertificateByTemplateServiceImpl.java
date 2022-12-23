package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.softserve.teachua.dto.certificate.CertificateTransfer;
import com.softserve.teachua.service.CertificateByTemplateService;
import com.softserve.teachua.utils.QRCodeService;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CertificateByTemplateServiceImpl implements CertificateByTemplateService {
    private static final String HALVAR_BLK_PATH =
        "./src/main/resources/certificates/fonts/Halvar Breitschrift Medium.ttf";
    private static final String HALVAR_MD_PATH =
        "./src/main/resources/certificates/fonts/Halvar Breitschrift Medium.ttf";

    @Autowired
    private QRCodeService qrCodeService;

    @Override
    public List<String> getTemplateFields(String templatePath) throws IOException {

        PdfReader reader = new PdfReader(templatePath);
        try (PdfDocument pdfDoc = new PdfDocument(reader)) {
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDoc, false);
            Map<String, PdfFormField> fields = acroForm.getFormFields();

            return new ArrayList<>(fields.keySet());
        }
    }

    @Override
    public String createCertificateByTemplate(CertificateTransfer transfer) throws IOException {
        String targetFileName = ThreadLocalRandom.current().nextInt() + ".pdf";
        PdfReader reader = new PdfReader(
            new ClassPathResource("certificates/templates/pdf-templates").getFile().getPath() + "/" +
                transfer.getTemplate().getFilePath());

        File directory =
            new File(new ClassPathResource("/").getFile().getPath() + "/" + "temp");
        if (!directory.exists()) {
            directory.mkdir();
        }

        PdfWriter writer =
            new PdfWriter(new ClassPathResource("/").getFile().getPath() + "/" + "temp/" + targetFileName);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        try (Document document = new Document(pdfDoc)) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

            PdfFont halvarBlk = PdfFontFactory.createFont(HALVAR_BLK_PATH, PdfEncodings.IDENTITY_H);
            PdfFont halvarMd = PdfFontFactory.createFont(HALVAR_MD_PATH, PdfEncodings.IDENTITY_H);

            HashMap<String, String> templateProperties =
                new ObjectMapper().readValue(transfer.getTemplate().getProperties(), HashMap.class);
            HashMap<String, String> values =
                new ObjectMapper().readValue(transfer.getValues(), HashMap.class);

            for (Map.Entry<String, String> entry : templateProperties.entrySet()) {
                switch (entry.getValue()) {
                    case "serial_number":
                        setValue(form.getField(entry.getKey()), transfer.getSerialNumber().toString(), halvarBlk,
                            halvarMd);
                        break;
                    case "qrCode_white":
                    case "qrCode_black":
                        List<Float> position = Arrays.stream(
                                form.getField(entry.getKey()).getWidgets().get(0).getRectangle().toString()
                                    .replace("[", "").replace("]", "").split(" "))
                            .map(Float::valueOf).collect(Collectors.toList());

                        float width = position.get(2) - position.get(0);
                        float height = position.get(3) - position.get(1);
                        Image image = getQrCodeImage(transfer.getSerialNumber(), width, height, entry.getValue());
                        image.setFixedPosition(position.get(0), position.get(1));

                        document.add(image);
                        break;
                    default:
                        setValue(form.getField(entry.getKey()), values.get(entry.getKey()), halvarBlk, halvarMd);
                }
            }
            form.flattenFields();
        } catch (IOException e) {
            log.error("Error processing pdf template");
            throw e;
        }
        return new ClassPathResource("/").getFile().getPath() + "/" + "temp/" + targetFileName;
    }

    private void setValue(PdfFormField field, String value, PdfFont firstFont, PdfFont secondFont) {
        if (field.getFont().getFontProgram().toString().equals("HalvarBreit-Blk")) {
            field.setFont(firstFont);
        } else {
            field.setFont(secondFont);
        }

        field.setValue(value);
    }

    private Image getQrCodeImage(Long serialNumber, float width, float height, String parameter) {
        MatrixToImageConfig colorConfig;
        int transparent = new Color(0, 0, 0, 0).getRGB();
        if ("qrCode_black".equals(parameter)) {
            colorConfig = new MatrixToImageConfig(new Color(0, 0, 0).getRGB(), transparent);
        } else {
            colorConfig = new MatrixToImageConfig(new Color(255, 255, 255).getRGB(), transparent);
        }
        return new Image(ImageDataFactory.create(
            qrCodeService.getCertificateQrCodeAsStream(serialNumber, width, height, colorConfig), false));
    }

}
