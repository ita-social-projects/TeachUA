package com.softserve.teachua.dto.databaseTransfer;

import com.softserve.teachua.dto.databaseTransfer.model.CategoryExcel;
import com.softserve.teachua.dto.databaseTransfer.model.CenterExcel;
import com.softserve.teachua.dto.databaseTransfer.model.ClubExcel;
import com.softserve.teachua.dto.databaseTransfer.model.DistrictExcel;
import com.softserve.teachua.dto.databaseTransfer.model.LocationExcel;
import com.softserve.teachua.dto.databaseTransfer.model.StationExcel;
import com.softserve.teachua.model.ExcelCenterEntity;
import com.softserve.teachua.model.ExcelClubEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ExcelParsingData {
    private final List<ClubExcel> clubs = new ArrayList<>();
    private final List<CenterExcel> centers = new ArrayList<>();
    private final List<CategoryExcel> categories = new ArrayList<>();
    private final List<StationExcel> stations = new ArrayList<>();
    private final List<DistrictExcel> districts = new ArrayList<>();
    private final List<LocationExcel> locations = new ArrayList<>();

    private final List<ExcelCenterEntity> excelCenters = new ArrayList<>();
    private final List<ExcelClubEntity> excelClubs = new ArrayList<>();
}
