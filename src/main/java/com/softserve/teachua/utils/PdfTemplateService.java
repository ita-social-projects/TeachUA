package com.softserve.teachua.utils;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PdfTemplateService {

    public List<String> getTemplateFields(String templatePath) throws FileNotFoundException {
        try {
            PdfReader reader = new PdfReader(templatePath);
            PdfDocument pdfDoc = new PdfDocument(reader);
            PdfAcroForm acroForm = PdfAcroForm.getAcroForm(pdfDoc, false);
            Map<String, PdfFormField> fields = acroForm.getFormFields();

            pdfDoc.close();
            return new ArrayList<>(fields.keySet());
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

}
