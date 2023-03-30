package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.constants.MessageType;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateSavingResponse;
import com.softserve.teachua.dto.certificate_by_template.CertificateByTemplateTransfer;
import com.softserve.teachua.exception.BadRequestException;
import com.softserve.teachua.exception.JsonWriteException;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import static com.softserve.teachua.service.CertificateDataLoaderService.getCertificateByTemplateValue;
import com.softserve.teachua.service.CertificateDatesService;
import com.softserve.teachua.service.CertificateGoogleFormService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.service.CertificateValidator;
import static com.softserve.teachua.service.impl.CertificateValidatorImpl.COURSE_NUMBER_ERROR;
import static com.softserve.teachua.service.impl.CertificateValidatorImpl.HOURS_ERROR;
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
                    String value = getCertificateByTemplateValue(values, data, i, "",
                            data.getFieldsList().get(j));
                    if (certificateValidator.validateNaturalNumber(value, messageList, COURSE_NUMBER_ERROR,
                            String.format(" Значення: \"%s\"", value))) {
                        certificateDates.setCourseNumber(value);
                    } else {
                        certificateErrors = true;
                    }
                    continue;
                } else if (data.getFieldsList().get(j).equals("Електронна пошта")) {
                    String value = getCertificateByTemplateValue(values, data, i, "email",
                            data.getFieldsList().get(j));
                    if (certificateValidator.validateEmail(value, messageList,
                            String.format("Значення: \"%s\"", value))) {
                        certificate.setSendToEmail(value);
                    } else {
                        certificateErrors = true;
                    }
                    continue;
                } else if (templateProperties.get(data.getFieldsList().get(j)).equals("user_name")) {
                    String value =
                            getCertificateByTemplateValue(values, data, i, "user_name",
                                    data.getFieldsList().get(j));
                    if (certificateValidator.validateUserName(value, messageList,
                            String.format(" Значення: \"%s\"", value))) {
                        certificate.setUserName(value);
                    } else {
                        certificateErrors = true;
                    }
                    continue;
                }

                String value = getCertificateByTemplateValue(values, data, i, "",
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
