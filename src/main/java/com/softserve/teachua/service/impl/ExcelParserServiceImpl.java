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
    private final ExcelCenterEntityRepository excelCenterEntityRepository;
    private final ExcelClubEntityRepository excelClubEntityRepository;


    @Autowired
    public ExcelParserServiceImpl(CityService cityService, DistrictService districtService,
                                  ExcelCenterEntityRepository excelCenterEntityRepository,
                                  ExcelClubEntityRepository excelClubEntityRepository) {
        this.cityService = cityService;
        this.districtService = districtService;
        this.excelCenterEntityRepository = excelCenterEntityRepository;
        this.excelClubEntityRepository = excelClubEntityRepository;
    }

    public ExcelParsingResponse parseExcel(InputStream excelInputStream) throws IOException {

        ExcelParsingResponse result = new ExcelParsingResponse();

        XSSFWorkbook excelBook = new XSSFWorkbook(excelInputStream);

        result.getSheetRowsCount().put(
                CENTER_SHEET_NAME,
                parseCenters(excelBook, result.getData().getCenters(),
                        result.getData().getLocations(),
                        result.getParsingMistakes())
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
                parseClubs(excelBook, result.getData().getClubs(),
                        result.getData().getLocations(),
                        result.getParsingMistakes())
        );


        parseExcelCenters(excelBook, result.getData().getExcelCenters(),
                        result.getParsingMistakes());


        parseExcelClubs(excelBook, result.getData().getExcelClubs(),
                        result.getParsingMistakes());

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

    private Long parseExcelClubs(XSSFWorkbook excelBook, List<ExcelClubEntity> clubEntities,
                                 List<ExcelParsingMistake> mistakesOutput){

        return parseSheet(excelBook, CLUB_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            log.info("CLUB REPRESENTATION");
            log.info("name = "+rowParser.getString(1, ExcelErrorType.CRITICAL));
            log.info("club_ext_ID = "+rowParser.getString(13, ExcelErrorType.CRITICAL));
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
                    .categories(rowParser.getString(10,ExcelErrorType.CRITICAL))
                    .ages(rowParser.getString(11,ExcelErrorType.CRITICAL))
                    .description(rowParser.getString(12, ExcelErrorType.CRITICAL))
                    .clubExternalId(rowParser.getLong(13, ExcelErrorType.CRITICAL))
                    .build();

            log.info(clubRepresentation.toString());

            if(!clubRepresentation.getName().isEmpty() & !clubRepresentation.getCoordinates().isEmpty()){

                excelClubEntityRepository.save(clubRepresentation);
            }

            clubEntities.add(clubRepresentation);

            return !rowParser.hasErrors();
        });

    }

    private Long parseExcelCenters(XSSFWorkbook excelBook, List<ExcelCenterEntity> centerEntities,
                                 List<ExcelParsingMistake> mistakesOutput){

        return parseSheet(excelBook, CENTER_SHEET_NAME, (row) -> {

            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            log.info("=CENTER REPRESENTATION ==>");
            log.info("name = "+rowParser.getString(1, ExcelErrorType.CRITICAL));

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
                    .description(rowParser.getString(9, ExcelErrorType.CRITICAL))
                    .build();

            log.info(centerRepresentation.toString());
            if(!centerRepresentation.getName().isEmpty() && !centerRepresentation.getCoordinates().isEmpty()){

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
        for (int i = 1; ; i++) {
            XSSFRow row = sheet.getRow(i);

            if (row == null)
                break;

            if (rowProcessing.test(row))
                totalCount++;
        }
        return totalCount;
    }

    private Long parseCenters(XSSFWorkbook excelBook, List<CenterExcel> centersOutput,
                              List<LocationExcel> locationsOutput,
                              List<ExcelParsingMistake> mistakesOutput) {

        return parseSheet(excelBook, CENTER_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            //todo delete comment
            log.info("parseCenter in row : "+row.getRowNum());
//            log.info("===////==="+row.toString());
            if (rowParser.isColumnEmpty(1)) {
                if(rowParser.isColumnEmpty(4)){
                    return false;
                }
                Double[] coordinates = rowParser.parseCoordinates(4, true);
                log.info("centerCoordinates in NEXT center location: " + Arrays.toString(coordinates));

                LocationExcel locationExcel = LocationExcel.builder()
                        .clubId(null)
                        .centerId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                        .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                        .address(rowParser.getString(3, true, ExcelErrorType.CRITICAL))
                        .longitude(coordinates[0])
                        .latitude(coordinates[1])
                        .district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                        .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL))
                        .name("center_location_"+rowParser.getString(3, true, ExcelErrorType.CRITICAL).substring(0,10))
                        .build();

                locationsOutput.add(locationExcel);
                return !rowParser.hasErrors();
            }
            Double[] coordinates = rowParser.parseCoordinates(4, true);
            log.info("centerCoordinates : " + Arrays.toString(coordinates));
            log.info("center contacts in excelParser: "+rowParser.getString(7, ExcelErrorType.NON_CRITICAL)+" , "
            +rowParser.getString(8, ExcelErrorType.CRITICAL));
            CenterExcel centerExcel = CenterExcel.builder()
//                    .id(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .name(rowParser.getString(1, ExcelErrorType.CRITICAL))

                    .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                    .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                    .description(rowParser.getString(9, ExcelErrorType.CRITICAL))
                    .build();

            LocationExcel locationExcel = LocationExcel.builder()
                    .clubId(null)
                    .centerId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                    .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                    .address(rowParser.getString(3, true, ExcelErrorType.CRITICAL))
                    .longitude(coordinates[0])
                    .latitude(coordinates[1])
                    .district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                    .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL))
                    .name("center_location_"+rowParser.getString(3, true, ExcelErrorType.CRITICAL).substring(0,9))
                    .build();
            centersOutput.add( centerExcel);
            locationsOutput.add(locationExcel);
            return !rowParser.hasErrors();
        });
    }

    private Long parseClubs(XSSFWorkbook excelBook, List<ClubExcel> clubExcels,
                            List<LocationExcel> locationsOutput,
                            List<ExcelParsingMistake> mistakesOutput) {
        return parseSheet(excelBook, CLUB_SHEET_NAME, (row) -> {
            ExcelRowParser rowParser = new ExcelRowParser(mistakesOutput, row);

            if ( rowParser.isColumnEmpty(0)) {
                //this club has no center and has its own locations
                if (rowParser.isColumnEmpty(1) ) { //if club has no name !

                    if(rowParser.isColumnEmpty(4)){ //and has no coordinates -- return false !!!
                        return false;

                    }else{
                        Double[] coordinates = rowParser.parseCoordinates(4);

                        Integer[] ages = rowParser.parseAges(11);

                        ClubExcel clubExcel = ClubExcel.builder()
                                .id(rowParser.getLong(13, ExcelErrorType.NON_CRITICAL))
                                .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                                .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                                .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                                .categories(rowParser.parseCategories(10))
                                .ageFrom(ages[0])
                                .ageTo(ages[1])
                                .description(rowParser.getString(12, ExcelErrorType.CRITICAL))
                                .build();

                        clubExcels.add(clubExcel);

                        LocationExcel location = LocationExcel.builder()
                                .centerId(null)
                                .clubId(rowParser.getLong(13, ExcelErrorType.NON_CRITICAL))
                                .name("club_loc_"+rowParser.getString(3, ExcelErrorType.CRITICAL))
                                .city(rowParser.getString(2, ExcelErrorType.CRITICAL))
                                .address(rowParser.getString(3, ExcelErrorType.CRITICAL))
                                .longitude(coordinates[0])
                                .latitude(coordinates[1])
                                .district(rowParser.getString(5, ExcelErrorType.NON_CRITICAL))
                                .station(rowParser.getString(6, ExcelErrorType.NON_CRITICAL))
                                .build();

                        log.info("new club without center,  Location : "+location.getAddress());
                        locationsOutput.add(location);
                        return !rowParser.hasErrors();
                    }
                }

            }else{
                // the case when club has center and we can skip parsing coordinates
                Integer[] ages = rowParser.parseAges(11);

                ClubExcel clubExcel = ClubExcel.builder()
                        .id(rowParser.getLong(13, ExcelErrorType.NON_CRITICAL))
                        .centerId(rowParser.getLong(0, ExcelErrorType.CRITICAL))
                        .name(rowParser.getString(1, ExcelErrorType.CRITICAL))
                        .site(rowParser.getString(7, ExcelErrorType.NON_CRITICAL))
                        .phone(rowParser.getString(8, ExcelErrorType.CRITICAL))
                        .categories(rowParser.parseCategories(10))
                        .ageFrom(ages[0])
                        .ageTo(ages[1])
                        .description(rowParser.getString(12, ExcelErrorType.CRITICAL))
                        .build();

                log.info("new club with center Location : "+clubExcel.getCenterId());
                clubExcels.add(clubExcel);
                return !rowParser.hasErrors();
            }

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
