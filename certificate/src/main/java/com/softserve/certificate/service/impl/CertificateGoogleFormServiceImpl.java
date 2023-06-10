package com.softserve.certificate.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.certificate.constants.MessageType;
import com.softserve.certificate.dto.certificate_by_template.CertificateByTemplateSavingResponse;
import com.softserve.certificate.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.certificate.model.Certificate;
import com.softserve.certificate.model.CertificateDates;
import com.softserve.certificate.model.CertificateTemplate;
import com.softserve.certificate.service.CertificateDataLoaderService;
import com.softserve.certificate.service.CertificateDatesService;
import com.softserve.certificate.service.CertificateGoogleFormService;
import com.softserve.certificate.service.CertificateService;
import com.softserve.certificate.service.CertificateTemplateService;
import com.softserve.certificate.service.CertificateValidator;
import static com.softserve.certificate.service.impl.CertificateValidatorImpl.COURSE_NUMBER_ERROR;
import static com.softserve.certificate.service.impl.CertificateValidatorImpl.HOURS_ERROR;
import com.softserve.commons.exception.BadRequestException;
import com.softserve.commons.exception.JsonWriteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CertificateGoogleFormServiceImpl implements CertificateGoogleFormService {
    private final CertificateService certificateService;
    private final CertificateTemplateService templateService;
    private final CertificateDatesService certificateDatesService;
    private final CertificateValidator certificateValidator;
    private final ObjectMapper objectMapper;

    public CertificateGoogleFormServiceImpl(CertificateService certificateService,
                                            CertificateTemplateService templateService,
                                            CertificateDatesService certificateDatesService,
                                            CertificateValidator certificateValidator,
                                            ObjectMapper objectMapper) {
        this.certificateService = certificateService;
        this.templateService = templateService;
        this.certificateDatesService = certificateDatesService;
        this.certificateValidator = certificateValidator;
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings({"squid:S3776", "squid:S135"}) //Suppressed because of project's business logic.
    public CertificateByTemplateSavingResponse saveGoogleFormCertificateData(CertificateByTemplateTransfer data) {
        List<Pair<String, MessageType>> messageList = new LinkedList<>();
        List<Certificate> validCertificates = new ArrayList<>();
        List<Map<String, String>> invalidCertificateValues = new ArrayList<>();

        CertificateTemplate certificateTemplate = templateService.getTemplateByFilePath(data.getTemplateName());

        HashMap<String, String> templateProperties;
        HashMap<String, String> mainValues;
        try {
            templateProperties = objectMapper.readValue(certificateTemplate.getProperties(), HashMap.class);
            mainValues = objectMapper.readValue(data.getValues(), HashMap.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing json to HashMap<String, String>");
            throw new BadRequestException();
        }

        for (int i = 0; i < data.getGoogleFormResults().size(); i++) {
            HashMap<String, String> values = new HashMap<>(mainValues);
            CertificateDates certificateDates = new CertificateDates();
            Certificate certificate = new Certificate();
            boolean certificateErrors = false;

            for (int j = 0; j < data.getFieldsList().size(); j++) {
                if (data.getFieldsList().get(j).equals("Номер курсу")) {
                    String value = CertificateDataLoaderService.getCertificateByTemplateValue(values, data, i, "",
                            data.getFieldsList().get(j));
                    if (certificateValidator.validateNaturalNumber(value, messageList, COURSE_NUMBER_ERROR,
                            String.format(" Значення: \"%s\"", value))) {
                        certificateDates.setCourseNumber(value);
                    } else {
                        certificateErrors = true;
                    }
                    continue;
                } else if (data.getFieldsList().get(j).equals("Електронна пошта")) {
                    String value = CertificateDataLoaderService.getCertificateByTemplateValue(values, data, i, "email",
                            data.getFieldsList().get(j));
                    if (certificateValidator.validateEmail(value, messageList,
                            String.format("Значення: \"%s\"", value))) {
                        certificate.setUserEmail(value);
                    } else {
                        certificateErrors = true;
                    }
                    continue;
                } else if (templateProperties.get(data.getFieldsList().get(j)).equals("user_name")) {
                    String value =
                            CertificateDataLoaderService.getCertificateByTemplateValue(values, data, i, "user_name",
                                    data.getFieldsList().get(j));
                    if (certificateValidator.validateUserName(value, messageList,
                            String.format(" Значення: \"%s\"", value))) {
                        certificate.setUserName(value);
                    } else {
                        certificateErrors = true;
                    }
                    continue;
                }

                String value = CertificateDataLoaderService.getCertificateByTemplateValue(values, data, i, "",
                        data.getFieldsList().get(j));

                if (value.trim().isEmpty()) {
                    messageList.add(Pair.of(
                            String.format("\"%s\" повинен бути заповнений", data.getFieldsList().get(j)),
                            MessageType.ERROR));
                    certificateErrors = true;
                    continue;
                }

                String messageDescription =
                        String.format("\"%s\". Значення \"%s\"", data.getFieldsList().get(j), value);
                // @formatter:off
                switch (templateProperties.get(data.getFieldsList().get(j))) {
                  case "course_number":
                      if (certificateValidator.validateNaturalNumber(value, messageList, COURSE_NUMBER_ERROR,
                              messageDescription)) {
                          certificateDates.setCourseNumber(value);
                      } else {
                          certificateErrors = true;
                      }
                      break;
                  case "date":
                      if (certificateValidator.validateDate(value, messageList, messageDescription)) {
                          certificateDates.setDate(value);
                      } else {
                          certificateErrors = true;
                      }
                      break;
                  case "hours":
                      if (certificateValidator.validateNaturalNumber(value, messageList, HOURS_ERROR,
                              messageDescription)) {
                          certificateDates.setHours(Integer.valueOf(value));
                      } else {
                          certificateErrors = true;
                      }
                      break;
                  default:
                      break;
                }
                // @formatter:on
            }

            if (certificateErrors) {
                invalidCertificateValues.add(values);
            } else {
                certificateDates = certificateDatesService.getOrCreateCertificateDates(certificateDates);

                try {
                    certificate.setValues(objectMapper.writeValueAsString(values));
                } catch (JsonProcessingException e) {
                    log.error("Error converting certificate values to json");
                    throw new JsonWriteException();
                }
                certificate.setTemplate(certificateTemplate);
                certificate.setDates(certificateDates);
                if (!certificateService.existsByUserNameAndDates(certificate.getUserName(), certificate.getDates())) {
                    validCertificates.add(certificate);
                } else {
                    messageList.add(Pair.of(
                            String.format("Сертифікат для \"%s\" уже додано", certificate.getUserName()),
                            MessageType.ERROR));
                }
            }
        }

        certificateService.addCertificates(validCertificates);
        if (!validCertificates.isEmpty()) {
            messageList.add(0, Pair.of(
                    String.format("Збережено %d сертифікатів із %d", validCertificates.size(),
                            data.getGoogleFormResults().size()), MessageType.SUCCESS));
        } else {
            log.warn("Detected invalid certificates {}", invalidCertificateValues);
            messageList.add(0, Pair.of(
                    String.format("Збережено %d сертифікатів із %d", validCertificates.size(),
                            data.getGoogleFormResults().size()), MessageType.WARNING));
        }
        return CertificateByTemplateSavingResponse.builder()
                .messages(messageList)
                .invalidValues(invalidCertificateValues)
                .build();
    }
}
