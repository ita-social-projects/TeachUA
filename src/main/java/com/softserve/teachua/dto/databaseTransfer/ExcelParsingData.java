package com.softserve.teachua.dto.databaseTransfer;

import com.softserve.teachua.dto.databaseTransfer.model.*;
import com.softserve.teachua.model.ExcelCenterEntity;
import com.softserve.teachua.model.ExcelClubEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExcelParsingData {

    private final List<ClubExcel> clubs = new ArrayList<>();
    private final List<CenterExcel> centers = new ArrayList<>();
    private final List<CategoryExcel> categories = new ArrayList<>();
    private final List<StationExcel> stations = new ArrayList<>();
    private final List<DistrictExcel> districts = new ArrayList<>();
    private final List<LocationExcel> locations = new ArrayList<>();

    private final List<ExcelCenterEntity> excelCenters = new ArrayList<>();
    private final List<ExcelClubEntity> excelClubs= new ArrayList<>();


}