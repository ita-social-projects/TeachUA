package com.softserve.teachua.utils;

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
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PdfTemplateService {
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

    public void sendSingleCertificate(CertificateByTemplateTransfer data) throws IOException {

        PdfReader reader = new PdfReader("templates/" + data.getTemplateName());
        PdfWriter writer = new PdfWriter("final.pdf");
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        try (Document document = new Document(pdfDoc)) {
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

            PdfFont halvarBlk = PdfFontFactory.createFont(HALVAR_BLK_PATH, PdfEncodings.IDENTITY_H);
            PdfFont halvarMd = PdfFontFactory.createFont(HALVAR_MD_PATH, PdfEncodings.IDENTITY_H);

            for (int i = 0; i < data.getFieldsList().size(); i++) {
                String value = data.getInputtedValues().get(i);
                String fieldName = data.getFieldsList().get(i);

                if (value.contains("~qr ")) {
                    value = value.replace("~qr ", "");
                    List<Float> position = Arrays.stream(
                            form.getField(fieldName).getWidgets().get(0).getRectangle().toString().replace("[", "")
                                .replace("]", "").split(" "))
                        .map(Float::valueOf).collect(Collectors.toList());

                    float width = position.get(2) - position.get(0);
                    float height = position.get(3) - position.get(1);

                    Image image = new Image(ImageDataFactory.create(
                        qrCodeService.getCertificateQrCodeAsStream(Long.valueOf(value), width, height), false));
                    image.setFixedPosition(position.get(0), position.get(1));

                    document.add(image);
                } else {
                    setValue(form.getField(fieldName), value,
                        halvarBlk, halvarMd);
                }
            }
            form.flattenFields();
        }
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
