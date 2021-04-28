package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingResponse;
import com.softserve.teachua.dto.databaseTransfer.model.*;
import com.softserve.teachua.service.CityService;
import com.softserve.teachua.service.DistrictService;
import com.softserve.teachua.service.ExcelParserService;
import com.softserve.teachua.utils.ExcelErrorType;
import com.softserve.teachua.utils.ExcelRowParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Vitalii Hapon
 */
@Service
@Slf4j
public class ExcelParserServiceImpl implements ExcelParserService {

    final String CENTER_SHEET_NAME = "Центр";
    final String CLUB_SHEET_NAME = "Гурток";
    final String DISTRICT_SHEET_NAME = "Райони";
    final String STATION_SHEET_NAME = "Метро";
    final String CATEGORY_SHEET_NAME = "Категорії";

    final CityService cityService;
    final DistrictService districtService;

    @Autowired
    public ExcelParserServiceImpl(CityService cityService, DistrictService districtService) {
        this.cityService = cityService;
        this.districtService = districtService;
    }

    public ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException {
        ExcelParsingResponse result = new ExcelParsingResponse();

        XSSFWorkbook excelBook = new XSSFWorkbook(excelInputStream);

        result.getSheetRowsCount().put(
                CENTER_SHEET_NAME,
                parseCenters(excelBook, result.getData().getCenters(), result.getParsingMistakes())
        );

        result.getSheetRowsCount().put(
                CATEGORY_SHEET_NAME,
                parseCategories(excelBook, result.getData().getCategories(), result.getParsingMistakes())
        );

        result.getSheetRowsCount().put(
                STATION_SHEET_NAME,
                parseStations(excelBook, result.getData().getStations(), result.getParsingMistakes())
        );

        result.getSheetRowsCount().put(
                DISTRICT_SHEET_NAME,
                parseDistricts(excelBook, result.getData().getDistricts(), result.getParsingMistakes())
        );

        result.getSheetRowsCount().put(
                CLUB_SHEET_NAME,
                parseClubs(excelBook, result.getData().getClubs(), result.getParsingMistakes())
        );

        excelBook.close();
        return result;
    }

    private String rowString(XSSFRow row, String sheetName, String columnName, int column
            , List<ExcelParsingMistake> mistakesOutput) {
        XSSFCell cell = row.getCell(column);
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                return cell.getStringCellValue();
            } else {
                return cell.getRawValue();
            }
        } else if (mistakesOutput != null) {
            mistakesOutput.add(ExcelParsingMistake.builder()
                    .cellValue("")
                    .rowIndex((long) row.getRowNum())
                    .columnName(columnName)
                    .errorDetails("Пустка клітинка")
                    .sheetName(sheetName)
                    .build()
            );

        }
        return "";
    }

    private Long parseSheet(XSSFWorkbook excelBook, String sheetName, Predicate<XSSFRow> rowProcessing) {
        XSSFSheet sheet = excelBook.getSheet(sheetName);

        Long totalCount = 0L;
        // 'i' starts from 1 because of header
        for (int i = 1; ; i++) {
            XSSFRow row = sheet.getRow(i);

            if (row == null)
                break;

            if (rowProcessing.test(row))
                totalCount++;
        }
        return totalCount;
    }

    private Long parseCenters(XSSFWorkbook excelBook, List<CenterExcel> centersOutput, List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CENTER_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            //todo delete comment
            //log.info(row.toString());
            if (rowParser.isColumnEmpty(1))
                return false;
            Double[] coordinates = rowParser.parseCoordinates(4, true);
            CenterExcel centerExcel = CenterExcel.builder()
                    .id(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                    .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                    .address(rowParser.getString(3, true, ExcelErrorType.CRITICAL))
                    .longitude(coordinates[0])
                    .altitude(coordinates[1])
                    .district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                    .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL))
                    .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                    .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                    .description(rowParser.getString(9, ExcelErrorType.CRITICAL))
                    .build();
            centersOutput.add( centerExcel);
            return !rowParser.hasErrors();
        });
    }

    private Long parseClubs(XSSFWorkbook excelBook, List<ClubExcel> clubExcels, List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CLUB_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            if (rowParser.isColumnEmpty(1))
                return false;
            Double[] coordinates = rowParser.parseCoordinates(4);
            Integer[] ages = rowParser.parseAges(10);
            ClubExcel clubExcel = ClubExcel.builder()
                    .id(rowParser.getLong(0, ExcelErrorType.NON_CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                    .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                    .address(rowParser.getString(3, ExcelErrorType.CRITICAL))
                    .longitude(coordinates[0])
                    .altitude(coordinates[1])
                    .district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                    .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL))
                    .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                    .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                    .categories(rowParser.parseCategories(9))
                    .ageFrom(ages[0])
                    .ageTo(ages[1])
                    .description(rowParser.getString(11, ExcelErrorType.CRITICAL))
                    .build();
            clubExcels.add(clubExcel);
            return !rowParser.hasErrors();
        });
    }

    private Long parseDistricts(XSSFWorkbook excelBook, List<DistrictExcel> centersOutput, List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, DISTRICT_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            centersOutput.add(
                    DistrictExcel.builder()
                            .city(rowParser.getString(0, ExcelErrorType.CRITICAL))
                            .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                            .build());
            return !rowParser.hasErrors();
        });
    }

    private Long parseStations(XSSFWorkbook excelBook, List<StationExcel> centersOutput, List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, STATION_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            centersOutput.add(
                    StationExcel.builder()
                            .city(rowParser.getString(0, ExcelErrorType.CRITICAL))
                            .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                            .build());
            return !rowParser.hasErrors();
        });
    }

    private Long parseCategories(XSSFWorkbook excelBook, List<CategoryExcel> centersOutput, List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CATEGORY_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            centersOutput.add(
                    CategoryExcel.builder()
                            .name(rowParser.getString(0, ExcelErrorType.CRITICAL))
                            .description(rowParser.getString(1, ExcelErrorType.CRITICAL))
                            .build());

            return !rowParser.hasErrors();
        });

    }

}
