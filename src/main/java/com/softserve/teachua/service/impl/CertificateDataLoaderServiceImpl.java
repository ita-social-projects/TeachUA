package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.certificate.CertificateDataRequest;
import com.softserve.teachua.dto.certificate.CertificateDatabaseResponse;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.model.Certificate;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateDatesService;
import com.softserve.teachua.service.CertificateService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.utils.CertificateContentDecorator;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CertificateDataLoaderServiceImpl implements CertificateDataLoaderService {
    private final static String UPDATED_EMAIL = "Оновлено електронну адресу учасника %s";
    private final static String ALREADY_EXISTS = "Сертифікат для учасника %s вже згенеровано %s";
    private final static String DATE_FORMAT = "dd.MM.YYYY";
    private final CertificateService certificateService;
    private final CertificateDatesService certificateDatesService;
    private final CertificateTemplateService templateService;
    private final CertificateRepository certificateRepository;
    private final CertificateDatesRepository datesRepository;
    private final CertificateTemplateRepository templateRepository;
    private final CertificateContentDecorator decorator;

    @Autowired
    public CertificateDataLoaderServiceImpl(CertificateService certificateService,
                                            CertificateDatesService certificateDatesService, CertificateTemplateService templateService,
                                            CertificateRepository certificateRepository, CertificateDatesRepository datesRepository,
                                            CertificateTemplateRepository templateRepository, CertificateContentDecorator decorator) {
        this.certificateService = certificateService;
        this.certificateDatesService = certificateDatesService;
        this.templateService = templateService;
        this.certificateRepository = certificateRepository;
        this.datesRepository = datesRepository;
        this.templateRepository = templateRepository;
        this.decorator = decorator;
    }

    @Override
    public List<CertificateDatabaseResponse> saveToDatabase(CertificateDataRequest data) {
        List<CertificateDatabaseResponse> response = new ArrayList<>();
        int index = 0;
        for (CertificateExcel certificateExcel : data.getExcelList()) {
            CertificateDates dates = saveDates(data, index);
            CertificateTemplate template = saveTemplate(data.getType());
            Certificate certificate = Certificate.builder().userName(certificateExcel.getName())
                    .sendToEmail(certificateExcel.getEmail()).template(template).dates(dates).build();
            if (!certificateRepository.existsByUserNameAndDates(certificate.getUserName(), certificate.getDates())) {
                certificateService.addCertificate(certificate);
            } else {
                Certificate certificateFound = certificateService.getByUserNameAndDates(certificate.getUserName(),
                        certificate.getDates());
                if (!certificate.getSendToEmail().equals(certificateFound.getSendToEmail())) {
                    certificate.setSendStatus(null);
                    certificateService.updateCertificateEmail(certificateFound.getId(), certificate);
                    response.add(new CertificateDatabaseResponse(
                            String.format(UPDATED_EMAIL, certificateFound.getUserName())));
                } else if (!certificate.getDates().equals(certificateFound.getDates())) {
                    certificateService.addCertificate(certificate);
                } else {
                    response.add(new CertificateDatabaseResponse(String.format(ALREADY_EXISTS,
                            certificateFound.getUserName(), certificateFound.getDates().getDate())));
                }
            }
            index++;
        }
        return response;
    }

    private CertificateDates saveDates(CertificateDataRequest data, Integer index) {
        CertificateDates dates = CertificateDates.builder()
                .date(data.getExcelList().get(index).getDateIssued().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .hours(data.getHours())
                .courseNumber(data.getCourseNumber())
                .studyForm(data.getStudyType()).build();
        if (data.getType() == 3) {
            dates.setDuration(decorator.formDates(data.getStartDate(), data.getEndDate()));
            if (!datesRepository.existsByDurationAndAndDate(dates.getDuration(), dates.getDate())) {
                return certificateDatesService.addCertificateDates(dates);
            }
            return certificateDatesService.getCertificateDatesByDurationAndDate(dates.getDuration(), dates.getDate());
        }
        //if (!datesRepository.existsByDate(dates.getDate())) {
        if (!datesRepository.existsByHoursAndDate(dates.getHours(), dates.getDate())) {
            return certificateDatesService.addCertificateDates(dates);
        }
        //return certificateDatesService.getCertificateDatesByDate(dates.getDate());
        return certificateDatesService.getCertificateDatesByHoursAndDate(dates.getHours(), dates.getDate());
    }

    private CertificateTemplate saveTemplate(Integer type) {
        if (!templateRepository.existsBy()) {
            return templateService.addTemplate(CertificateTemplate.builder().name("Єдині учасник").certificateType(3)
                    .filePath("/certificates/templates/jedyni_participant_template.jrxml")
                    .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                    .projectDescription(
                            "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                    .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        if (!templateRepository.existsCertificateTemplateByCertificateType(1)) { // Trainer
            templateService.addTemplate(CertificateTemplate.builder().name("Єдині тренер").certificateType(1)
                    .filePath("/certificates/templates/trainer_certificate.jrxml")
                    .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                    .projectDescription(
                            "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                    .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        if (!templateRepository.existsCertificateTemplateByCertificateType(2)) { // Moderator
            templateService.addTemplate(CertificateTemplate.builder().name("Єдині модератор").certificateType(2)
                    .filePath("/certificates/templates/moderator_certificate.jrxml")
                    .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                    .projectDescription(
                            "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                    .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        if (!templateRepository.existsCertificateTemplateByCertificateType(3)) {
            templateService.addTemplate(CertificateTemplate.builder().name("Єдині учасник").certificateType(3)
                    .filePath("/certificates/templates/jedyni_participant_template.jrxml")
                    .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                    .projectDescription(
                            "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                    .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        //TODO
        switch (type) {
            case 1:
                return templateService.getTemplateByType(3);
            case 2:
                return templateService.getTemplateById(2);
            default:
                return templateService.getTemplateById(1);
        }
    }

    @Override
    public boolean saveCertificate(CertificateByTemplateTransfer data)
        throws JsonProcessingException {
        CertificateTemplate certificateTemplate =
            templateService.getTemplateByFilePath(data.getTemplateName());

        HashMap<String, String> templateProperties =
            new ObjectMapper().readValue(certificateTemplate.getProperties(), HashMap.class);
        HashMap<String, String> mainValues =
            new ObjectMapper().readValue(data.getValues(), HashMap.class);

        for (List<String> excelValues: data.getExcelContent()) {
            HashMap<String, String> values = new HashMap<>(mainValues);
            CertificateDates certificateDates = new CertificateDates();
            Certificate certificate = new Certificate();

            for (Map.Entry<String, String> entry : templateProperties.entrySet()) {
                switch (entry.getValue()) {
                    case "serial_number":
                        if (!templateProperties.containsValue("course_number")) {
                            certificateDates.setCourseNumber(
                                getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                    data.getExcelColumnsOrder(), excelValues, "Номер курсу"));
                        }
                        break;
                    case "course_number":
                        certificateDates.setCourseNumber(
                            getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "user_name":
                        certificate.setUserName(getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                            data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "date":
                        certificateDates.setDate(getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                            data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "duration":
                        certificateDates.setDuration(
                            getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "hours":
                        certificateDates.setHours(
                            Integer.valueOf(getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey())));
                        break;
                    case "study_form":
                        certificateDates.setStudyForm(
                            getValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                }
            }
            certificateDatesService.addCertificateDates(certificateDates);

            certificate.setValues(new ObjectMapper().writeValueAsString(values));
            certificate.setSendToEmail(
                getValue(values, data.getFieldsList(), data.getColumnHeadersList(), data.getExcelColumnsOrder(),
                    excelValues, "Електронна пошта"));
            certificate.setTemplate(certificateTemplate);
            certificate.setDates(certificateDates);

            certificateService.addCertificate(certificate);
        }
        return true;
    }

    private String getValue(Map<String, String> values, List<String> fieldsList, List<String> columnHeadersList,
                            List<String> excelColumnsOrder, List<String> excelValues, String propertyName) {
        String result = values.get(propertyName);
        if (!result.trim().isEmpty()) {
            return result;
        } else {
            String value =
                excelValues.get(columnHeadersList.indexOf(excelColumnsOrder.get(fieldsList.indexOf(propertyName))));
            values.put(propertyName, value);
            return value;
        }
    }

}
