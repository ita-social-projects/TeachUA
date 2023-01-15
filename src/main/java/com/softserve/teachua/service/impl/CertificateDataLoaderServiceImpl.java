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
import com.softserve.teachua.model.CertificateType;
import com.softserve.teachua.repository.CertificateDatesRepository;
import com.softserve.teachua.repository.CertificateRepository;
import com.softserve.teachua.repository.CertificateTemplateRepository;
import com.softserve.teachua.repository.CertificateTypeRepository;
import com.softserve.teachua.service.*;
import com.softserve.teachua.utils.CertificateContentDecorator;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CertificateDataLoaderServiceImpl implements CertificateDataLoaderService {
    private final static String UPDATED_EMAIL = "Оновлено електронну адресу учасника %s";
    private final static String ALREADY_EXISTS = "Сертифікат для учасника %s вже згенеровано %s";
    private final static String DATE_FORMAT = "dd.MM.YYYY";
    private final CertificateService certificateService;
    private final CertificateDatesService certificateDatesService;
    private final CertificateTypeService certificateTypeService;
    private final CertificateTemplateService templateService;
    private final CertificateRepository certificateRepository;
    private final CertificateDatesRepository datesRepository;
    private final CertificateTemplateRepository templateRepository;
    private final CertificateTypeRepository certificateTypeRepository;
    private final CertificateContentDecorator decorator;

    @Autowired
    public CertificateDataLoaderServiceImpl(CertificateService certificateService,
                                            CertificateDatesService certificateDatesService,
                                            CertificateTypeService certificateTypeService,
                                            CertificateTemplateService templateService,
                                            CertificateRepository certificateRepository,
                                            CertificateDatesRepository datesRepository,
                                            CertificateTemplateRepository templateRepository,
                                            CertificateTypeRepository certificateTypeRepository,
                                            CertificateContentDecorator decorator) {
        this.certificateService = certificateService;
        this.certificateDatesService = certificateDatesService;
        this.certificateTypeService = certificateTypeService;
        this.templateService = templateService;
        this.certificateRepository = certificateRepository;
        this.datesRepository = datesRepository;
        this.templateRepository = templateRepository;
        this.certificateTypeRepository = certificateTypeRepository;
        this.decorator = decorator;
    }

    @Override
    public List<CertificateDatabaseResponse> saveToDatabase(CertificateDataRequest data) {
        List<CertificateDatabaseResponse> response = new ArrayList<>();
        int index = 0;
        for (CertificateExcel certificateExcel : data.getExcelList()) {
            CertificateDates dates = saveDates(data, index);
            CertificateTemplate template = saveTemplate(data.getType());
            Certificate certificate =
                Certificate.builder().userName(certificateExcel.getName()).sendToEmail(certificateExcel.getEmail())
                    .template(template).dates(dates).build();
            if (!certificateRepository.existsByUserNameAndDates(certificate.getUserName(), certificate.getDates())) {
                certificateService.addCertificate(certificate);
            } else {
                Certificate certificateFound =
                    certificateService.getByUserNameAndDates(certificate.getUserName(), certificate.getDates());
                if (!certificate.getSendToEmail().equals(certificateFound.getSendToEmail())) {
                    certificate.setSendStatus(null);
                    certificateService.updateCertificateEmail(certificateFound.getId(), certificate);
                    response.add(
                        new CertificateDatabaseResponse(String.format(UPDATED_EMAIL, certificateFound.getUserName())));
                } else if (!certificate.getDates().equals(certificateFound.getDates())) {
                    certificateService.addCertificate(certificate);
                } else {
                    response.add(new CertificateDatabaseResponse(
                        String.format(ALREADY_EXISTS, certificateFound.getUserName(),
                            certificateFound.getDates().getDate())));
                }
            }
            index++;
        }
        return response;
    }

    private CertificateDates saveDates(CertificateDataRequest data, Integer index) {
        CertificateDates dates = CertificateDates.builder()
            .date(data.getExcelList().get(index).getDateIssued().format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
            .hours(data.getHours()).courseNumber(data.getCourseNumber()).studyForm(data.getStudyType()).build();
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
        if (!certificateTypeRepository.existsById(1)) {
            certificateTypeService.addCertificateType(
                CertificateType.builder().codeNumber(1).name("Тренер").build());
        }
        if (!certificateTypeRepository.existsById(2)) {
            certificateTypeService.addCertificateType(
                CertificateType.builder().codeNumber(2).name("Модератор").build());
        }
        if (!certificateTypeRepository.existsById(3)) {
            certificateTypeService.addCertificateType(
                CertificateType.builder().codeNumber(3).name("Учасник").build());
        }
        if (!certificateTypeRepository.existsById(4)) {
            certificateTypeService.addCertificateType(
                CertificateType.builder().codeNumber(4).name("Учасник").build());
        }
        CertificateType certificateType = certificateTypeService.getCertificateTypeById(3);
        if (!templateRepository.existsCertificateTemplateByCertificateType(certificateType)) {
            templateService.addTemplate(CertificateTemplate.builder().name("Єдині учасник")
                .certificateType(certificateType)
                .filePath("/certificates/templates/jedyni_participant_template.jrxml")
                .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                .projectDescription(
                    "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        certificateType = certificateTypeService.getCertificateTypeById(1);
        if (!templateRepository.existsCertificateTemplateByCertificateType(certificateType)) { // Trainer
            templateService.addTemplate(CertificateTemplate.builder().name("Єдині тренер")
                .certificateType(certificateType)
                .filePath("/certificates/templates/trainer_certificate.jrxml")
                .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                .projectDescription(
                    "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        certificateType = certificateTypeService.getCertificateTypeById(2);
        if (!templateRepository.existsCertificateTemplateByCertificateType(certificateType)) { // Moderator
            templateService.addTemplate(CertificateTemplate.builder().name("Єдині модератор")
                .certificateType(certificateType)
                .filePath("/certificates/templates/moderator_certificate.jrxml")
                .courseDescription("Всеукраїнський курс “Єдині. 28 днів підтримки в переході на українську мову”")
                .projectDescription(
                    "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        certificateType = certificateTypeService.getCertificateTypeById(4);
        if (!templateRepository.existsCertificateTemplateByCertificateType(certificateType)) {
            templateService.addTemplate(CertificateTemplate.builder().name("Учасник базового рівня")
                .certificateType(certificateType)
                .filePath("/certificates/templates/jedyni_basic_participant_template.jrxml")
                .courseDescription("Вивчення української мови базового рівня.").projectDescription(
                    "Курс створений та реалізований у рамках проєкту “Єдині” ініціативи “Навчай українською”, до якої належить “Українська гуманітарна платформа”.")
                .picturePath("/static/images/certificate/validation/jedyni_banner.png").build());
        }
        return templateService.getTemplateByType(type);
    }

    @Override
    public void saveCertificate(CertificateByTemplateTransfer data) throws JsonProcessingException {
        CertificateTemplate certificateTemplate = templateService.getTemplateByFilePath(data.getTemplateName());

        HashMap<String, String> templateProperties =
            new ObjectMapper().readValue(certificateTemplate.getProperties(), HashMap.class);
        HashMap<String, String> mainValues = new ObjectMapper().readValue(data.getValues(), HashMap.class);
        boolean excelProcessing = false;
        int i = 1;
        if (!data.getExcelContent().isEmpty()) {
            i = data.getExcelContent().size();
            excelProcessing = true;
        }
        for (int j = 0; j < i; j++) {
            HashMap<String, String> values = new HashMap<>(mainValues);
            CertificateDates certificateDates = new CertificateDates();
            Certificate certificate = new Certificate();

            List<String> excelValues = null;
            if (excelProcessing) {
                excelValues = data.getExcelContent().get(j);
            }
            for (Map.Entry<String, String> entry : templateProperties.entrySet()) {
                switch (entry.getValue()) {
                    case "serial_number":
                        if (!templateProperties.containsValue("course_number")) {
                            certificateDates.setCourseNumber(
                                getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                    data.getExcelColumnsOrder(), excelValues, "Номер курсу"));
                        }
                        break;
                    case "course_number":
                        certificateDates.setCourseNumber(
                            getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "user_name":
                        certificate.setUserName(
                            getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "date":
                        certificateDates.setDate(
                            getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "duration":
                        certificateDates.setDuration(
                            getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    case "hours":
                        certificateDates.setHours(Integer.valueOf(
                            getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey())));
                        break;
                    case "study_form":
                        certificateDates.setStudyForm(
                            getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                                data.getExcelColumnsOrder(), excelValues, entry.getKey()));
                        break;
                    default:
                        break;
                }
            }
            certificateDatesService.addCertificateDates(certificateDates);

            certificate.setValues(new ObjectMapper().writeValueAsString(values));
            certificate.setSendToEmail(
                getCertificateByTemplateValue(values, data.getFieldsList(), data.getColumnHeadersList(),
                    data.getExcelColumnsOrder(), excelValues, "Електронна пошта"));
            certificate.setTemplate(certificateTemplate);
            certificate.setDates(certificateDates);

            certificateService.addCertificate(certificate);
        }
    }

    @Override
    public String getCertificateByTemplateValue(Map<String, String> values, List<String> fieldsList,
                                                List<String> columnHeadersList, List<String> excelColumnsOrder,
                                                List<String> excelValues, String propertyName) {
        String result = values.get(propertyName);
        if (result.trim().isEmpty()) {
            result =
                excelValues.get(columnHeadersList.indexOf(excelColumnsOrder.get(fieldsList.indexOf(propertyName))));
            values.put(propertyName, result);
        }
        return result;
    }

}
