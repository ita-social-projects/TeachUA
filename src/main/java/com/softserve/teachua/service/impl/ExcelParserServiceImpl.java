package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.databaseTransfer.ExcelParsingMistake;
import com.softserve.teachua.dto.databaseTransfer.ExcelParsingResponse;
import com.softserve.teachua.dto.databaseTransfer.model.*;
import com.softserve.teachua.model.ExcelCenterEntity;
import com.softserve.teachua.model.ExcelClubEntity;
import com.softserve.teachua.repository.ExcelCenterEntityRepository;
import com.softserve.teachua.repository.ExcelClubEntityRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Service
@Slf4j
public class ExcelParserServiceImpl implements ExcelParserService {
    private static final String CENTER_SHEET_NAME = "Центр";
    private static final String CLUB_SHEET_NAME = "Гурток";
    private static final String DISTRICT_SHEET_NAME = "Райони";
    private static final String STATION_SHEET_NAME = "Метро";
    private static final String CATEGORY_SHEET_NAME = "Категорії";

    final CityService cityService;
    final DistrictService districtService;
    private final ExcelCenterEntityRepository excelCenterEntityRepository;
    private final ExcelClubEntityRepository excelClubEntityRepository;

    private String previousName = "";

    @Autowired
    public ExcelParserServiceImpl(CityService cityService, DistrictService districtService,
            ExcelCenterEntityRepository excelCenterEntityRepository,
            ExcelClubEntityRepository excelClubEntityRepository) {
        this.cityService = cityService;
        this.districtService = districtService;
        this.excelCenterEntityRepository = excelCenterEntityRepository;
        this.excelClubEntityRepository = excelClubEntityRepository;
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

        // todo loading data to tmp excel representation tables

        excelBook.close();
        return result;
    }

    private String rowString(XSSFRow row, String sheetName, String columnName, int column,
            List<ExcelParsingMistake> mistakesOutput) {
        XSSFCell cell = row.getCell(column);
        if (cell != null) {
            if (cell.getCellType() == CellType.STRING && !cell.getStringCellValue().isEmpty()) {
                return cell.getStringCellValue();
            } else {
                return cell.getRawValue();
            }
        } else if (mistakesOutput != null) {
            mistakesOutput.add(ExcelParsingMistake.builder().cellValue("").rowIndex((long) row.getRowNum())
                    .columnName(columnName).errorDetails("Пустка клітинка").sheetName(sheetName).build());
        }
        return "";
    }

    private Long parseExcelClubs(XSSFWorkbook excelBook, List<ExcelClubEntity> clubEntities,
            List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CLUB_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            log.debug("CLUB REPRESENTATION");
            log.debug("name = " + rowParser.getString(1, ExcelErrorType.CRITICAL));
            log.debug("club_ext_ID = " + rowParser.getString(13, ExcelErrorType.CRITICAL));
            ExcelClubEntity clubRepresentation = ExcelClubEntity.builder()

                    .centerExternalId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                    .cityName(rowParser.getString(2, ExcelErrorType.CRITICAL))
                    .address(rowParser.getString(3, ExcelErrorType.CRITICAL))
                    .coordinates(rowParser.getString(4, ExcelErrorType.CRITICAL))
                    .district(rowParser.getString(5, ExcelErrorType.CRITICAL))
                    .station(rowParser.getString(6, ExcelErrorType.CRITICAL))
                    .webContact(rowParser.getString(7, ExcelErrorType.CRITICAL))
                    .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                    .categories(rowParser.getString(10, ExcelErrorType.CRITICAL))
                    .ages(rowParser.getString(11, ExcelErrorType.CRITICAL))
                    .description(rowParser.getString(12, ExcelErrorType.CRITICAL))
                    .clubExternalId(rowParser.getLong(13, ExcelErrorType.CRITICAL)).build();

            log.debug(clubRepresentation.toString());

            excelClubEntityRepository.save(clubRepresentation);
            clubEntities.add(clubRepresentation);

            return !rowParser.hasErrors();
        });
    }

    private Long parseExcelCenters(XSSFWorkbook excelBook, List<ExcelCenterEntity> centerEntities,
            List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CENTER_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            log.debug("=CENTER REPRESENTATION ==>");
            log.debug("name = " + rowParser.getString(1, ExcelErrorType.CRITICAL));

            ExcelCenterEntity centerRepresentation = ExcelCenterEntity.builder()
                    .centerExternalId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                    .cityName(rowParser.getString(2, ExcelErrorType.CRITICAL))
                    .address(rowParser.getString(3, ExcelErrorType.CRITICAL))
                    .coordinates(rowParser.getString(4, ExcelErrorType.CRITICAL))
                    .district(rowParser.getString(5, ExcelErrorType.CRITICAL))
                    .station(rowParser.getString(6, ExcelErrorType.CRITICAL))
                    .webContact(rowParser.getString(7, ExcelErrorType.CRITICAL))
                    .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                    .description(rowParser.getString(9, ExcelErrorType.CRITICAL)).build();

            log.debug("ROW(194, ExcelParser)   === parse Center .... centerExcel : ");
            log.debug(centerRepresentation.toString());
            log.debug("END==========================");
            if (!centerRepresentation.getName().isEmpty() && !centerRepresentation.getCoordinates().isEmpty()) {
                excelCenterEntityRepository.save(centerRepresentation);
            }
            centerEntities.add(centerRepresentation);
            return !rowParser.hasErrors();
        });
    }

    private Long parseSheet(XSSFWorkbook excelBook, String sheetName, Predicate<XSSFRow> rowProcessing) {
        XSSFSheet sheet = excelBook.getSheet(sheetName);

        Long totalCount = 0L;
        // 'i' starts from 1 because of header
        for (int i = 1;; i++) {
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

    private Long parseCenters(XSSFWorkbook excelBook, List<CenterExcel> centersOutput,
            List<LocationExcel> locationsOutput, List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CENTER_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            log.debug("=CENTER REPRESENTATION ==>");
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
                                                                                                                 // previous
                                                                                                                 // name
                        .build();

                // centersOutput.add(centerExcel);
                locationsOutput.add(locationExcel);
                return !rowParser.hasErrors();
            }
            Double[] coordinates = rowParser.parseCoordinates(4, true);
            log.debug("centerCoordinates : " + Arrays.toString(coordinates));
            log.debug("center contacts in excelParser: " + rowParser.getString(7, ExcelErrorType.NON_CRITICAL) + " , "
                    + rowParser.getString(8, ExcelErrorType.CRITICAL));
            log.debug("==  centerExt_Id :  " + rowParser.getLong(0, ExcelErrorType.CRITICAL));

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

            log.debug("ROW(283, ExcelParser)   === parse Center .... centerExcel : ");
            log.debug(centerExcel.toString());
            log.debug("END==========================");

            centersOutput.add(centerExcel);
            locationsOutput.add(locationExcel);

            return !rowParser.hasErrors();
        });
    }

    private Long parseClubs(XSSFWorkbook excelBook, List<ClubExcel> clubExcels, List<LocationExcel> locationsOutput,
            List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CLUB_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            if (rowParser.isColumnEmpty(0)) {
                log.debug("club without center !!!!!!!!!");
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
                log.debug(" (row 345,ExelParser..).... club has center   !!!!!! ");
                // the case when club has center and we can skip parsing coordinates
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
            // return !rowParser.hasErrors();
        });
    }

    private Long parseDistricts(XSSFWorkbook excelBook, List<DistrictExcel> districtsOutput,
            List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, DISTRICT_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            districtsOutput.add(DistrictExcel.builder().city(rowParser.getString(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL)).build());
            return !rowParser.hasErrors();
        });
    }

    private Long parseStations(XSSFWorkbook excelBook, List<StationExcel> stationsOutput,
            List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, STATION_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            stationsOutput.add(StationExcel.builder().city(rowParser.getString(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL)).build());
            return !rowParser.hasErrors();
        });
    }

    private Long parseCategories(XSSFWorkbook excelBook, List<CategoryExcel> categoriesOutput,
            List<ExcelParsingMistake> mistakesOutput) {
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
