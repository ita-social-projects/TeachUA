package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.databaseTransfer.ExcelFullParsingMistake;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingResponse;
import com.softserve.teachua.dto.databaseTransfer.model.CategoryExcel;
import com.softserve.teachua.dto.databaseTransfer.model.CenterExcel;
import com.softserve.teachua.dto.databaseTransfer.model.ClubExcel;
import com.softserve.teachua.dto.databaseTransfer.model.DistrictExcel;
import com.softserve.teachua.dto.databaseTransfer.model.LocationExcel;
import com.softserve.teachua.dto.databaseTransfer.model.StationExcel;
import com.softserve.teachua.service.ExcelParserService;
import com.softserve.teachua.constants.excel.ExcelErrorType;
import com.softserve.teachua.constants.excel.ExcelColumn;
import com.softserve.teachua.utils.ExcelRowParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class ExcelParserServiceImpl implements ExcelParserService {
    private static final String CENTER_SHEET_NAME = "Центр";
    private static final String CLUB_SHEET_NAME = "Гурток";
    private static final String DISTRICT_SHEET_NAME = "Райони";
    private static final String STATION_SHEET_NAME = "Метро";
    private static final String CATEGORY_SHEET_NAME = "Категорії";
    private static final String FILE_NOT_READ_EXCEPTION = "Неможливо прочитати Excel файл";

    private final DataFormatter dataFormatter;
    private String previousName;

    @Autowired
    public ExcelParserServiceImpl(DataFormatter dataFormatter) {
        this.dataFormatter = dataFormatter;
        previousName = "";
    }

    @Override
    public ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException {
        ExcelParsingResponse result = new ExcelParsingResponse();

        XSSFWorkbook excelBook = new XSSFWorkbook(excelInputStream);

        result.getSheetRowsCount().put(CENTER_SHEET_NAME, parseCenters(excelBook, result.getData().getCenters(),
                result.getData().getLocations(), result.getParsingMistakes()));

        result.getSheetRowsCount().put(CATEGORY_SHEET_NAME,
                parseCategories(excelBook, result.getData().getCategories(), result.getParsingMistakes()));

        result.getSheetRowsCount().put(STATION_SHEET_NAME,
                parseStations(excelBook, result.getData().getStations(), result.getParsingMistakes()));

        result.getSheetRowsCount().put(DISTRICT_SHEET_NAME,
                parseDistricts(excelBook, result.getData().getDistricts(), result.getParsingMistakes()));

        result.getSheetRowsCount().put(CLUB_SHEET_NAME, parseClubs(excelBook, result.getData().getClubs(),
                result.getData().getLocations(), result.getParsingMistakes()));

        excelBook.close();
        return result;
    }

    private Long parseSheet(XSSFWorkbook excelBook, String sheetName, Predicate<XSSFRow> rowProcessing) {
        XSSFSheet sheet = excelBook.getSheet(sheetName);

        Long totalCount = 0L;
        // 'i' starts from 1 because of header
        for (int i = 1; ; i++) {
            XSSFRow row = sheet.getRow(i);

            if (row == null) {
                break;
            }

            if (rowProcessing.test(row)) {
                totalCount++;
            }
        }
        return totalCount;
    }

    @Override
    public Sheet getSheetFromExcelFile(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            return workbook.getSheetAt(0);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, FILE_NOT_READ_EXCEPTION);
        }
    }

    @Override
    public boolean isRowEmpty(Row row) {
        boolean isEmpty = true;
        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    @Override
    public boolean isColumnEmpty(Sheet sheet, int columnIndex) {
        for (Row row : sheet) {
            Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<List<Cell>> excelToList(MultipartFile multipartFile) {
        Sheet sheet = getSheetFromExcelFile(multipartFile);
        List<List<Cell>> allCells = new ArrayList<>();

        for (Row row : sheet) {
            if (isRowEmpty(row)) {
                continue;
            }
            List<Cell> allRowCells = new ArrayList<>();
            for (Cell currentCell : row) {
                if (!isColumnEmpty(sheet, currentCell.getColumnIndex())) {
                    allRowCells.add(currentCell);
                }
            }
            allCells.add(allRowCells);
        }
        return allCells;
    }

    @Override
    public HashMap<ExcelColumn, Integer> getColumnIndexes(List<Cell> headerRow, ExcelColumn[] excelColumns) {
        HashMap<ExcelColumn, Integer> indexes = new HashMap<>();
        for (Cell cell : headerRow) {
            String lowerCaseCell = dataFormatter.formatCellValue(cell).toLowerCase();
            for (ExcelColumn column : excelColumns) {
                if (lowerCaseCell.contains(column.getKeyWord())) {
                    indexes.put(column, cell.getColumnIndex());
                }
            }
        }
        return indexes;
    }

    @Override
    public List<ExcelParsingMistake> validateColumnsPresent(List<Cell> headerRow, ExcelColumn[] excelColumns,
                                                             HashMap<ExcelColumn, Integer> indexes) {
        return Arrays.stream(excelColumns)
                .filter(column -> !indexes.containsKey(column))
                .map(column -> new ExcelParsingMistake(column.getMissingMessage(), headerRow.toString(), 1))
                .collect(Collectors.toList());
    }

    private Long parseCenters(XSSFWorkbook excelBook, List<CenterExcel> centersOutput,
                              List<LocationExcel> locationsOutput, List<ExcelFullParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CENTER_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            log.debug("name = " + rowParser.getString(1, ExcelErrorType.CRITICAL));

            if (rowParser.isColumnEmpty(1)) {
                if (rowParser.isColumnEmpty(4)) {
                    return false;
                }
                Double[] coordinates = rowParser.parseCoordinates(4, true);
                log.debug("centerCoordinates in NEXT center location: " + Arrays.toString(coordinates));

                LocationExcel locationExcel = LocationExcel.builder().clubExternalId(null)
                        .centerExternalId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                        .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                        .address(rowParser.getString(3, true, ExcelErrorType.CRITICAL)).longitude(coordinates[0])
                        .latitude(coordinates[1]).district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                        .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL)).name("center_location___") // Add
                        .build();

                locationsOutput.add(locationExcel);
                return !rowParser.hasErrors();
            }
            Double[] coordinates = rowParser.parseCoordinates(4, true);
            log.debug("centerCoordinates : " + Arrays.toString(coordinates));
            log.debug("center contacts in excelParser: " + rowParser.getString(7, ExcelErrorType.NON_CRITICAL) + " , "
                    + rowParser.getString(8, ExcelErrorType.CRITICAL));

            previousName = rowParser.getString(1, ExcelErrorType.CRITICAL);

            CenterExcel centerExcel = CenterExcel.builder()
                    .centerExternalId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                    .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                    .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                    .description(rowParser.getString(9, ExcelErrorType.CRITICAL)).build();

            LocationExcel locationExcel = LocationExcel.builder().clubExternalId(null)
                    .centerExternalId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                    .address(rowParser.getString(3, true, ExcelErrorType.CRITICAL)).longitude(coordinates[0])
                    .latitude(coordinates[1]).district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                    .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL)).name("center_location_first.....")
                    .build();
            locationsOutput.add(locationExcel);

            log.debug(centerExcel.toString());
            centersOutput.add(centerExcel);
            return !rowParser.hasErrors();
        });
    }

    private Long parseClubs(XSSFWorkbook excelBook, List<ClubExcel> clubExcels, List<LocationExcel> locationsOutput,
                            List<ExcelFullParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CLUB_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            if (rowParser.isColumnEmpty(0)) {
                // this club has no center and has its own locations
                if (rowParser.isColumnEmpty(1) && rowParser.isColumnEmpty(4)) {
                    // and has no coordinates -- return false !!!
                    return false;
                } else {
                    Double[] coordinates = rowParser.parseCoordinates(4);

                    Integer[] ages = rowParser.parseAges(11);

                    String name = rowParser.getString(1, ExcelErrorType.CRITICAL);

                    if (name == null || name.isEmpty()) {
                        name = previousName;
                    } else {
                        previousName = name;
                    }
                    if (!rowParser.isColumnEmpty(1)) {
                        ClubExcel clubExcel = ClubExcel.builder()
                                .clubExternalId(rowParser.getLong(13, ExcelErrorType.NON_CRITICAL)).name(name)
                                .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                                .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                                .categories(rowParser.parseCategories(10)).ageFrom(ages[0]).ageTo(ages[1])
                                .description(rowParser.getString(12, ExcelErrorType.CRITICAL)).build();
                        clubExcels.add(clubExcel);
                    }

                    LocationExcel location = LocationExcel.builder().centerExternalId(null)
                            .clubExternalId(rowParser.getLong(13, ExcelErrorType.NON_CRITICAL)).name("club_loc_!!!")
                            .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                            .address(rowParser.getString(3, ExcelErrorType.CRITICAL)).longitude(coordinates[0])
                            .latitude(coordinates[1]).district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                            .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL)).build();

                    log.debug("new club without center,  Location : " + location.getAddress());
                    locationsOutput.add(location);
                    return !rowParser.hasErrors();
                }
            } else {
                // the case when club has center, and we can skip parsing coordinates
                Integer[] ages = rowParser.parseAges(11);

                String name = rowParser.getString(1, ExcelErrorType.NON_CRITICAL);

                if (name == null || name.isEmpty()) {
                    name = previousName;
                } else {
                    previousName = name;
                }

                ClubExcel clubExcel = ClubExcel.builder()
                        .clubExternalId(rowParser.getLong(13, ExcelErrorType.NON_CRITICAL))
                        .centerExternalId(rowParser.getLong(0, ExcelErrorType.CRITICAL)).name(name)
                        .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                        .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                        .categories(rowParser.parseCategories(10)).ageFrom(ages[0]).ageTo(ages[1])
                        .description(rowParser.getString(12, ExcelErrorType.CRITICAL)).build();

                log.debug("new club with center's Locations : " + clubExcel.getCenterExternalId());

                clubExcels.add(clubExcel);

                return !rowParser.hasErrors();
            }
        });
    }

    private Long parseDistricts(XSSFWorkbook excelBook, List<DistrictExcel> districtsOutput,
                                List<ExcelFullParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, DISTRICT_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            districtsOutput.add(DistrictExcel.builder().city(rowParser.getString(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL)).build());
            return !rowParser.hasErrors();
        });
    }

    private Long parseStations(XSSFWorkbook excelBook, List<StationExcel> stationsOutput,
                               List<ExcelFullParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, STATION_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            stationsOutput.add(StationExcel.builder().city(rowParser.getString(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL)).build());
            return !rowParser.hasErrors();
        });
    }

    private Long parseCategories(XSSFWorkbook excelBook, List<CategoryExcel> categoriesOutput,
                                 List<ExcelFullParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CATEGORY_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            String name = rowParser.getString(0, ExcelErrorType.CRITICAL).trim();
            String description = rowParser.getString(1, ExcelErrorType.CRITICAL).trim();
            if (!name.isEmpty() && !description.isEmpty()) {
                categoriesOutput.add(CategoryExcel.builder().name(name).description(description).build());
            }
            return !rowParser.hasErrors();
        });
    }
}
