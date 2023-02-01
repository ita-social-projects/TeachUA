package com.softserve.teachua.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.teachua.dto.certificateByTemplate.CertificateByTemplateTransfer;
import com.softserve.teachua.dto.certificateExcel.CertificateByTemplateExcelParsingResponse;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import com.softserve.teachua.dto.certificateExcel.ExcelParsingResponse;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.service.CertificateDataLoaderService;
import com.softserve.teachua.service.CertificateExcelService;
import com.softserve.teachua.service.CertificateTemplateService;
import com.softserve.teachua.service.ExcelParserService;
import com.softserve.teachua.utils.excel.enums.CertificateExcelColumn;
import com.softserve.teachua.utils.excel.ExcelColumn;
import static com.softserve.teachua.utils.validations.CertificateUserNameValidator.NAME_PATTERN;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class CertificateExcelServiceImpl implements CertificateExcelService {
    private static final String INCORRECT_DATE_FORMAT_ERROR = "Неможливо розпізнати дату видачі сертифікату";
    private static final String DATE_FORMAT = "[d.MM.yyyy][MM/d/yy][MM/d/yyyy]";
    private static final String WORD = "([А-ЯІЇЄ][а-яіїє']+[-–—]?){1,2}";
    private static final String INVALID_CHARACTERS_PRESENT = "Присутні недопустимі літери";
    private static final String INVALID_CHARS = "\\w";
    private static final String USER_NAME_ERROR = "Неправильний формат ПІБ!";
    private static final String USER_NAME_FORMAT = NAME_PATTERN;
    private static final String DATE_ERROR =
            "Неправильний формат дати. Будь ласка, використовуйте наступний формат: \"dd.mm.yyyy\"!";
    private static final String DATE_FORMAT_REGEX = "[0-9]{2}.[0-9]{2}.[0-9]{4}";
    private static final String HOURS_ERROR = "Неправильний формат кількості годин!";
    private static final String COURSE_NUMBER_ERROR = "Неправильний формат номера курсу!";
    private static final String EMAIL_ERROR = "Неправильний формат електронної пошти!";
    private static final String EMAIL_FORMAT = "[\\w-.]+@([\\w-]+\\.){1,61}+[\\w-]{2,4}";

    private final DataFormatter dataFormatter;
    private final CertificateTemplateService templateService;
    private final CertificateDataLoaderService dataLoaderService;
    private final ExcelParserService excelParserService;
    private HashMap<ExcelColumn, Integer> indexes;
    private ExcelParsingResponse response;

    public CertificateExcelServiceImpl(DataFormatter dataFormatter, CertificateTemplateService templateService,
                                       CertificateDataLoaderService dataLoaderService,
                                       ExcelParserService excelParserService) {
        this.dataFormatter = dataFormatter;
        this.templateService = templateService;
        this.dataLoaderService = dataLoaderService;
        this.excelParserService = excelParserService;
    }

    @Override
    public ExcelParsingResponse parseExcel(MultipartFile multipartFile) {
        response = new ExcelParsingResponse();
        List<List<Cell>> rows = excelParserService.excelToList(multipartFile);
        indexes = excelParserService.getColumnIndexes(rows.get(0), CertificateExcelColumn.values());
        response.getParsingMistakes().addAll(
                excelParserService.validateColumnsPresent(rows.get(0), CertificateExcelColumn.values(), indexes));
        if (response.getParsingMistakes().isEmpty()) {
            response.setCertificatesInfo(createUserCertificates(rows));
        }
        return response;
    }

    @Override
    public CertificateByTemplateExcelParsingResponse parseFlexibleExcel(MultipartFile multipartFile) {
        List<List<Cell>> allCells = excelParserService.excelToList(multipartFile);

        List<List<String>> allConvertedCells =
                allCells.stream().map(cellList ->
                        cellList.stream().map(element -> {
                            String value = "";
                            // @formatter:off
                            switch (element.getCellType()) {
                              case STRING:
                                  return element.getRichStringCellValue().getString();
                              case NUMERIC:
                                  if (DateUtil.isCellDateFormatted(element)) {
                                      return String.valueOf(element.getDateCellValue());
                                  } else {
                                      return String.valueOf((int) element.getNumericCellValue());
                                  }
                              default:
                                  return value;
                            }
                            // @formatter:on
                        }).collect(Collectors.toList())
                ).collect(Collectors.toList());

        return CertificateByTemplateExcelParsingResponse.builder()
                .columnHeadersList(allConvertedCells.remove(0))
                .excelContent(allConvertedCells)
                .build();
    }

    private CertificateExcel createUserCertificate(List<Cell> row) {
        int rowIndex = row.get(0).getRowIndex() + 1;

        String name = getName(row, rowIndex);
        LocalDate date = getDate(row, rowIndex);
        String email = getEmail(row);

        CertificateExcel certificateExcel = CertificateExcel.builder()
                .name(name)
                .dateIssued(date)
                .email(email)
                .build();
        validateCertificateExcel(certificateExcel, rowIndex);
        return certificateExcel;
    }

    private String getEmail(List<Cell> row) {
        Cell emailCell = row.get(indexes.get(CertificateExcelColumn.EMAIL));
        return dataFormatter.formatCellValue(emailCell).trim();
    }

    private LocalDate getDate(List<Cell> row, int rowIndex) {
        Cell dateCell = row.get(indexes.get(CertificateExcelColumn.DATE));
        String stringDate = dataFormatter.formatCellValue(dateCell).trim();
        LocalDate date = null;
        try {
            date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (DateTimeParseException e) {
            response.getParsingMistakes()
                    .add(new ExcelParsingMistake(INCORRECT_DATE_FORMAT_ERROR, stringDate, rowIndex));
        }
        return date;
    }

    private String getName(List<Cell> row, int rowIndex) {
        Cell nameCell = row.get(indexes.get(CertificateExcelColumn.SURNAME));
        Pattern pattern = Pattern.compile(INVALID_CHARS);
        Matcher matcher = pattern.matcher(nameCell.toString());
        if (matcher.find()) {
            response.getParsingMistakes().add(
                    new ExcelParsingMistake(INVALID_CHARACTERS_PRESENT, nameCell.toString(), rowIndex));
        }
        return formUserName(nameCell);
    }

    @Override
    public List<CertificateExcel> createUserCertificates(List<List<Cell>> rows) {
        List<CertificateExcel> result = new ArrayList<>();
        rows.remove(0);
        for (List<Cell> row : rows) {
            result.add(createUserCertificate(row));
        }
        return result;
    }

    private String formUserName(Cell nameCell) {
        StringBuilder validatedName = new StringBuilder();
        String name = dataFormatter.formatCellValue(nameCell).trim();
        Matcher matcher = Pattern.compile(WORD).matcher(name);
        while (matcher.find()) {
            validatedName.append(matcher.group()).append(" ");
        }
        return validatedName.toString().trim();
    }

    private void validateCertificateExcel(CertificateExcel certificateExcel, int rowIndex) {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<CertificateExcel>> violations = validator.validate(certificateExcel);
        for (ConstraintViolation<CertificateExcel> violation : violations) {
            response.getParsingMistakes().add(
                    new ExcelParsingMistake(violation.getMessage(), violation.getInvalidValue().toString(), rowIndex));
        }
    }

    @Override
    public List<String[]> validateCertificateByTemplateExcel(CertificateByTemplateTransfer data)
            throws JsonProcessingException {
        List<String[]> resultList = new ArrayList<>();

        CertificateTemplate certificateTemplate = templateService.getTemplateByFilePath(data.getTemplateName());

        HashMap<String, String> templateProperties =
                new ObjectMapper().readValue(certificateTemplate.getProperties(), HashMap.class);
        HashMap<String, String> values = new ObjectMapper().readValue(data.getValues(), HashMap.class);

        for (int i = 0; i < data.getExcelContent().size(); i++) {
            List<String> excelValues = data.getExcelContent().get(i);

            for (int j = 0; j < data.getFieldsList().size(); j++) {
                String value = dataLoaderService.getCertificateByTemplateValue(values, data.getFieldsList(),
                        data.getColumnHeadersList(), data.getExcelColumnsOrder(), excelValues,
                        data.getFieldsList().get(j));
                String messageDescription =
                        (!values.get(data.getFieldsList().get(j)).isEmpty() ? " Рядок " + (i + 2) + ". " : "")
                                + "Значення \"" + value + "\".";

                if (value.trim().isEmpty()) {
                    resultList.add(new String[] {"Рядок " + (j + 2) + ". " + "Пуста клітинка.", "2"});
                    continue;
                }
                // @formatter:off
                switch (data.getFieldsList().get(j)) {
                  case "Номер курсу":
                      int courseNumber;
                      try {
                          courseNumber = Integer.parseInt(value);
                      } catch (RuntimeException e) {
                          resultList.add(new String[]{COURSE_NUMBER_ERROR + messageDescription, "2"});
                          continue;
                      }
                      if (courseNumber <= 0) {
                          resultList.add(new String[]{COURSE_NUMBER_ERROR + messageDescription, "2"});
                      }
                      continue;
                  case "Електронна пошта":
                      if (!value.matches(EMAIL_FORMAT)) {
                          resultList.add(new String[]{EMAIL_ERROR + messageDescription, "2"});
                      }
                      continue;
                  default:
                      break;
                }
                switch (templateProperties.get(data.getFieldsList().get(j))) {
                  case "course_number":
                      int courseNumber;
                      try {
                          courseNumber = Integer.parseInt(value);
                      } catch (RuntimeException e) {
                          resultList.add(new String[]{COURSE_NUMBER_ERROR + messageDescription, "2"});
                          break;
                      }
                      if (courseNumber <= 0) {
                          resultList.add(new String[]{COURSE_NUMBER_ERROR + messageDescription, "2"});
                      }
                      break;
                  case "user_name":
                      if (!value.matches(USER_NAME_FORMAT)) {
                          StringBuilder stringBuilder = new StringBuilder();
                          if (value.contains("  ")) {
                              stringBuilder.append(" Подвійний пробіл!");
                          }
                          if (value.trim().length() < value.length()) {
                              stringBuilder.append(" ПІБ починається/закінчується пробілом!");
                          }
                          resultList.add(new String[]{USER_NAME_ERROR + messageDescription + stringBuilder, "1"});
                      }
                      break;
                  case "date":
                      if (!value.matches(DATE_FORMAT_REGEX)) {
                          resultList.add(new String[]{DATE_ERROR + messageDescription, "1"});
                      }
                      break;
                  case "hours":
                      int hours;
                      try {
                          hours = Integer.parseInt(value);
                      } catch (RuntimeException e) {
                          resultList.add(new String[]{HOURS_ERROR + messageDescription, "2"});
                          break;
                      }
                      if (hours <= 0) {
                          resultList.add(new String[]{HOURS_ERROR + messageDescription, "2"});
                      }
                      break;
                  default:
                      break;
                }
                // @formatter:on
            }
        }
        if (resultList.isEmpty()) {
            resultList.add(new String[] {"Валідація пройшла успішно!", "3"});
        }
        return resultList;
    }
}
