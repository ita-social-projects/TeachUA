package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.service.CertificateByTemplateService;
import com.softserve.teachua.utils.QRCodeService;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        PdfReader reader = new PdfReader("templates/" + transfer.getTemplate().getFilePath());
        PdfWriter writer = new PdfWriter("temp/" + targetFileName);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        try (Document document = new Document(pdfDoc)) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

            PdfFont halvarBlk = PdfFontFactory.createFont(HALVAR_BLK_PATH, PdfEncodings.IDENTITY_H);
            PdfFont halvarMd = PdfFontFactory.createFont(HALVAR_MD_PATH, PdfEncodings.IDENTITY_H);
            System.out.println(transfer.getTemplate().getFilePath());
            System.out.println(targetFileName);

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
                    case "qrCode":
                        List<Float> position = Arrays.stream(
                                form.getField(entry.getKey()).getWidgets().get(0).getRectangle().toString().
                                    replace("[", "").replace("]", "").split(" "))
                            .map(Float::valueOf).collect(Collectors.toList());

                        float width = position.get(2) - position.get(0);
                        float height = position.get(3) - position.get(1);
                        Image image = new Image(ImageDataFactory.create(
                            qrCodeService.getCertificateQrCodeAsStream(transfer.getSerialNumber(), width, height),
                            false));
                        image.setFixedPosition(position.get(0), position.get(1));

                        document.add(image);
                        break;
                    default:
                        setValue(form.getField(entry.getKey()), values.get(entry.getKey()), halvarBlk, halvarMd);
                }
            }
            form.flattenFields();
        }
        return "temp/" + targetFileName;
    }

    private void setValue(PdfFormField field, String value, PdfFont firstFont, PdfFont secondFont) {
        if (field.getFont().getFontProgram().toString().equals("HalvarBreit-Blk")) {
            field.setFont(firstFont);
        } else {
            field.setFont(secondFont);
        }

        field.setValue(value);
    }

}
